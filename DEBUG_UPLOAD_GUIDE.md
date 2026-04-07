# Debug Guide: Cloudinary Image Upload & Display

## Build Status
✅ **BUILD SUCCESSFUL** - App đã được build với đầy đủ logging

## Mục Tiêu
Đảm bảo ảnh được upload lên Cloudinary thành công và hiển thị trên giao diện HomeScreen

## Logging Đã Thêm

### 1. CloudinaryService.kt
```
🔵 Cloudinary: Starting upload...
🔵 Cloudinary: File size: X bytes
🔵 Cloudinary: Cloud Name: dwzsc2t3i
🔵 Cloudinary: Upload Preset: ml_default
🔵 Cloudinary: Upload URL: https://api.cloudinary.com/v1_1/dwzsc2t3i/image/upload
✅ Cloudinary: Upload SUCCESS!
✅ Cloudinary: URL: [Cloudinary URL]
✅ Cloudinary: Public ID: [public_id]
```

**Hoặc nếu thất bại:**
```
❌ Cloudinary: Upload FAILED!
❌ Cloudinary: Error: [error message]
```

### 2. AdminRepository.kt
```
📤 Repository: Starting upload for image_XXXXX.jpg
📤 Repository: File size: X bytes
✅ Repository: Upload successful!
✅ Repository: Cloudinary URL: https://res.cloudinary.com/...
💾 Repository: Adding note to Firestore
💾 Repository: Title: [title]
💾 Repository: File URL: [URL]
✅ Repository: Note added to Firestore successfully!
```

### 3. NoteViewModel.kt
```
🎯 ViewModel: addNote called
🎯 ViewModel: Title: [title]
🎯 ViewModel: Has image: true
🎯 ViewModel: File size: X bytes
🎯 ViewModel: Starting upload...
🎯 ViewModel: Upload completed. URL: [URL]
🎯 ViewModel: Saving to Firestore with URL: [URL]
✅ ViewModel: Note saved successfully!
```

### 4. HomeScreen.kt (NoteCard)
```
🖼️ NoteCard: Note '[title]' has image
🖼️ NoteCard: URL = https://res.cloudinary.com/...
🖼️ NoteCard: Starts with https? true
🖼️ NoteCard: Attempting to load image from: [URL]
🔄 NoteCard: Loading image...
```

**Nếu load thất bại:**
```
❌ NoteCard: Failed to load image!
❌ NoteCard: Error: [exception]
```

## Testing Steps

### 1. Cài Đặt App Mới
```bash
# Xóa app cũ
adb uninstall org.example.btvnkotlin

# Build APK mới
.\gradlew :composeApp:assembleDebug

# Cài đặt
adb install composeApp\build\outputs\apk\debug\composeApp-debug.apk
```

### 2. Mở Logcat Để Theo Dõi
```bash
# Clear logcat
adb logcat -c

# Theo dõi real-time (filter theo tag System.out)
adb logcat | Select-String -Pattern "Cloudinary|Repository|ViewModel|NoteCard"
```

### 3. Test Upload Flow

**Bước 1:** Mở app và login
**Bước 2:** Click nút "+" để thêm note mới
**Bước 3:** Click "Chọn Ảnh" → Chọn một ảnh

**Kiểm tra trong AddEditScreen:**
- Preview ảnh có hiển thị không?
- Có thấy card với filename và file size không?

**Bước 4:** Nhập title và description
**Bước 5:** Click "ADD ITEM"

**Trong Logcat, bạn nên thấy chuỗi log sau:**

```
🎯 ViewModel: addNote called
🎯 ViewModel: Has image: true
🎯 ViewModel: File size: [số bytes]
📤 Repository: Starting upload for image_XXXXX.jpg
🔵 Cloudinary: Starting upload...
🔵 Cloudinary: File size: X bytes
🔵 Cloudinary: Upload URL: https://api.cloudinary.com/v1_1/dwzsc2t3i/image/upload
✅ Cloudinary: Upload SUCCESS!
✅ Cloudinary: URL: https://res.cloudinary.com/dwzsc2t3i/...
✅ Repository: Upload successful!
💾 Repository: Adding note to Firestore
✅ Repository: Note added to Firestore successfully!
✅ ViewModel: Note saved successfully!
```

**Bước 6:** Quay lại HomeScreen

**Trong Logcat, bạn nên thấy:**
```
🖼️ NoteCard: Note '[title]' has image
🖼️ NoteCard: URL = https://res.cloudinary.com/...
🖼️ NoteCard: Attempting to load image from: [URL]
🔄 NoteCard: Loading image...
```

**Bước 7:** Kiểm tra giao diện
- ✅ Ảnh thumbnail (80x80) hiển thị bên trái
- ✅ Icon "Có hình ảnh" màu cam hiển thị
- ✅ Title và description hiển thị bên phải

## Các Trường Hợp Lỗi Thường Gặp

### Lỗi 1: Upload Thất Bại - Unauthorized
```
❌ Cloudinary: Upload FAILED!
❌ Cloudinary: Error: 401 Unauthorized
```

**Nguyên nhân:** Upload preset sai hoặc không phải unsigned
**Giải pháp:**
1. Vào https://console.cloudinary.com/
2. Settings → Upload → Upload Presets
3. Kiểm tra "ml_default" có tồn tại và là "Unsigned"
4. Nếu không, tạo preset mới:
   - Click "Add upload preset"
   - Chọn "Unsigned"
   - Đặt tên: `ml_default`
   - Save

### Lỗi 2: Upload Thất Bại - Network Timeout
```
❌ Cloudinary: Upload FAILED!
❌ Cloudinary: Error: java.net.SocketTimeoutException
```

**Nguyên nhân:** Mất kết nối hoặc file quá lớn
**Giải pháp:**
1. Kiểm tra kết nối internet
2. Thử ảnh nhỏ hơn (< 1MB)
3. Tăng timeout trong CloudinaryService (hiện chưa có timeout config)

### Lỗi 3: Ảnh Upload Thành Công Nhưng Không Hiển Thị
```
✅ Cloudinary: Upload SUCCESS!
✅ Cloudinary: URL: https://res.cloudinary.com/...
🖼️ NoteCard: URL = https://res.cloudinary.com/...
❌ NoteCard: Failed to load image!
```

**Nguyên nhân:** Kamel không load được ảnh
**Giải pháp:**
1. Copy URL từ log
2. Paste vào browser để kiểm tra ảnh có mở được không
3. Nếu ảnh mở được trong browser nhưng không hiển thị trong app → vấn đề với Kamel
4. Check lại INTERNET permission trong AndroidManifest.xml

### Lỗi 4: File URL Rỗng
```
🎯 ViewModel: Upload completed. URL: 
💾 Repository: File URL: 
```

**Nguyên nhân:** Upload thất bại im lặng
**Giải pháp:**
1. Scroll up trong logcat tìm error message từ Cloudinary
2. Fix theo error message cụ thể

### Lỗi 5: Không Có Log Gì Cả
**Nguyên nhân:** `pendingFileBytes` là null
**Giải pháp:**
1. Kiểm tra xem ImagePicker có được gọi không
2. Xem có crash khi chọn ảnh không
3. Check permissions: READ_MEDIA_IMAGES, INTERNET

## Verify trên Cloudinary Dashboard

### 1. Mở Media Library
https://console.cloudinary.com/console/media_library

### 2. Check Folder "notes"
- Click vào folder "notes/"
- Ảnh vừa upload sẽ xuất hiện ở đây
- Click vào ảnh để xem details:
  - **Public ID:** notes/abc123
  - **URL:** https://res.cloudinary.com/dwzsc2t3i/...
  - **Format:** jpg
  - **Size:** X KB

### 3. Test URL Trực Tiếp
- Copy URL từ Cloudinary dashboard
- Paste vào browser
- ✅ Ảnh phải hiển thị được

## Verify trên Firestore

### 1. Mở Firebase Console
https://console.firebase.google.com/

### 2. Chọn Project: gk-mobile-25-26

### 3. Firestore Database → Notes Collection
- Tìm document vừa tạo (sort by timestamp)
- Click vào document
- Kiểm tra field `file`:
  ```
  file: "https://res.cloudinary.com/dwzsc2t3i/image/upload/v1234567890/notes/abc123.jpg"
  ```

## Expected Full URL Format
```
https://res.cloudinary.com/dwzsc2t3i/image/upload/v[timestamp]/notes/[filename].jpg
```

**Ví dụ:**
```
https://res.cloudinary.com/dwzsc2t3i/image/upload/v1744177200/notes/image_123456.jpg
```

## Success Criteria

✅ Trong logcat thấy:
- "✅ Cloudinary: Upload SUCCESS!"
- "✅ Repository: Upload successful!"
- "✅ ViewModel: Note saved successfully!"

✅ Trong Cloudinary dashboard thấy ảnh trong folder "notes/"

✅ Trong Firestore thấy field `file` có URL bắt đầu với "https://res.cloudinary.com/"

✅ Trong HomeScreen thấy thumbnail ảnh hiển thị

✅ Trong logcat KHÔNG thấy:
- "❌ Cloudinary: Upload FAILED!"
- "❌ NoteCard: Failed to load image!"

## Next Steps Sau Khi Test

1. **Nếu upload thành công:**
   - ✅ Chụp screenshot HomeScreen với ảnh
   - ✅ Xóa các println debug (optional)
   - ✅ App ready to use!

2. **Nếu upload thất bại:**
   - Copy toàn bộ error logs từ logcat
   - Gửi cho tôi để debug thêm
   - Check Cloudinary credentials lần nữa

3. **Nếu upload thành công nhưng không hiển thị:**
   - Copy Cloudinary URL từ log
   - Test URL trong browser
   - Kiểm tra INTERNET permission
   - Có thể thử Coil thay vì Kamel

## APK Location
```
D:\Lap Trinh\Nam 2 Ki 2\LT Mobile\code\BTVN Kotlin\composeApp\build\outputs\apk\debug\composeApp-debug.apk
```

## Install Command
```bash
adb install -r composeApp\build\outputs\apk\debug\composeApp-debug.apk
```

Bắt đầu test và cho tôi biết kết quả log bạn thấy! 🚀
