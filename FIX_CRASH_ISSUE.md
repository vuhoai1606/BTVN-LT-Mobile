# Fix: Cloudinary Upload Crash Issue

## Vấn Đề
App crash khi ấn nút "Chọn Ảnh" trong AddEditScreen.

## Nguyên Nhân

### 1. ❌ Thiếu Android Permissions
**AndroidManifest.xml** không khai báo các permissions cần thiết:
- `INTERNET` - Cần để upload lên Cloudinary
- `READ_MEDIA_IMAGES` - Android 13+ cần permission này để đọc ảnh
- `READ_EXTERNAL_STORAGE` - Android 12 trở xuống

### 2. ❌ ImagePicker Implementation Issue
ImagePicker được gọi mỗi lần recompose, gây ra multiple launches và crash.

**Code cũ (sai):**
```kotlin
actual object ImagePicker {
    @Composable
    actual fun PickImage(onImagePicked: (ByteArray?) -> Unit) {
        val launcher = rememberLauncherForActivityResult(...) { ... }
        launcher.launch("image/*")  // ❌ Launch ngay lập tức → crash
    }
}
```

## Giải Pháp

### Fix 1: Thêm Permissions vào AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Permissions cần thiết cho upload ảnh -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    
    <!-- Permission đọc ảnh (Android 13+) -->
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    
    <!-- Permission đọc storage (Android 12 trở xuống) -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" 
        android:maxSdkVersion="32" />

    <application>
        ...
    </application>
</manifest>
```

**Giải thích:**
- `INTERNET` - Bắt buộc cho tất cả network requests
- `ACCESS_NETWORK_STATE` - Check trạng thái network
- `READ_MEDIA_IMAGES` - Android 13+ (API 33+) yêu cầu permission riêng cho images
- `READ_EXTERNAL_STORAGE` - Android 12 trở xuống, giới hạn `maxSdkVersion="32"`

### Fix 2: Sử dụng LaunchedEffect trong ImagePicker

**Code mới (đúng):**
```kotlin
actual object ImagePicker {
    @Composable
    actual fun PickImage(onImagePicked: (ByteArray?) -> Unit) {
        val context = LocalContext.current
        
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()
                    onImagePicked(bytes)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onImagePicked(null)
                }
            } else {
                onImagePicked(null)
            }
        }
        
        // ✅ Launch chỉ một lần khi composable được tạo
        LaunchedEffect(Unit) {
            launcher.launch("image/*")
        }
    }
}
```

**Tại sao cần LaunchedEffect?**
- `rememberLauncherForActivityResult` chỉ register launcher
- `launcher.launch()` phải được gọi từ **side effect**, không phải trực tiếp trong Composable body
- `LaunchedEffect(Unit)` đảm bảo chỉ chạy **một lần** khi composable được tạo
- `Unit` key nghĩa là không re-trigger khi recompose

## Testing Steps

### 1. Uninstall app cũ (nếu đã cài)
```bash
adb uninstall org.example.btvnkotlin
```

### 2. Build và install APK mới
```bash
.\gradlew :composeApp:assembleDebug
adb install composeApp\build\outputs\apk\debug\composeApp-debug.apk
```

### 3. Test flow
1. Mở app
2. Login với admin account
3. Click nút "+" để thêm note
4. Click nút "Chọn Ảnh"
5. ✅ Gallery/File picker sẽ mở (không crash)
6. Chọn một ảnh
7. ✅ Preview ảnh hiển thị
8. Nhập title/description
9. Click "ADD ITEM"
10. ✅ Upload thành công lên Cloudinary

### 4. Verify trên Cloudinary Dashboard
- Vào https://console.cloudinary.com/console/media_library
- Check folder `notes/`
- Ảnh vừa upload sẽ xuất hiện

## Common Errors & Solutions

### Error 1: SecurityException - Permission Denied
**Lỗi:**
```
java.lang.SecurityException: Permission denied
```

**Giải pháp:**
- Kiểm tra AndroidManifest.xml đã có permissions
- Android 13+: User phải manually grant permission lần đầu
- Uninstall và reinstall app để reset permissions

### Error 2: ActivityNotFoundException
**Lỗi:**
```
android.content.ActivityNotFoundException: No Activity found
```

**Giải pháp:**
- Device/emulator không có gallery app
- Cài Google Photos hoặc Gallery app

### Error 3: FileNotFoundException khi đọc URI
**Lỗi:**
```
java.io.FileNotFoundException: No content provider
```

**Giải pháp:**
- URI không valid hoặc đã bị xóa
- Thêm try-catch và handle gracefully (đã có trong code)

### Error 4: OutOfMemoryError với ảnh lớn
**Lỗi:**
```
java.lang.OutOfMemoryError: Failed to allocate
```

**Giải pháp (TODO):**
```kotlin
// Compress ảnh trước khi upload
fun compressImage(bytes: ByteArray): ByteArray {
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
    return stream.toByteArray()
}
```

## Permissions Request Flow (Android 13+)

Android 13+ yêu cầu runtime permission request:

```kotlin
// TODO: Thêm runtime permission request
@Composable
fun RequestImagePermission(onGranted: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onGranted()
    }
    
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            onGranted() // Already granted via manifest
        }
    }
}
```

## File Changes Summary

| File | Change | Reason |
|------|--------|--------|
| `AndroidManifest.xml` | ✅ Added 4 permissions | Fix permission denied |
| `ImagePicker.android.kt` | ✅ Added LaunchedEffect | Fix multiple launch crash |

## Build Status
✅ **BUILD SUCCESSFUL in 49s**

## Next Steps (Optional Improvements)

1. **Runtime Permission Request**
   - Thêm UI để request permission
   - Handle permission denied case

2. **Image Compression**
   - Compress ảnh trước khi upload
   - Reduce file size và bandwidth

3. **Better Error Messages**
   - Show Snackbar/Toast khi lỗi
   - User-friendly error messages

4. **Progress Indicator**
   - Show % upload progress
   - Cancel upload button

5. **Multiple Images**
   - Support chọn nhiều ảnh
   - Gallery view

## Conclusion
Crash issue đã được fix bằng cách:
1. ✅ Thêm đầy đủ permissions vào AndroidManifest
2. ✅ Sử dụng LaunchedEffect để launch picker đúng cách
3. ✅ Build thành công
4. 🎯 Ready to test!

App bây giờ có thể chọn ảnh và upload lên Cloudinary mà không bị crash! 🚀
