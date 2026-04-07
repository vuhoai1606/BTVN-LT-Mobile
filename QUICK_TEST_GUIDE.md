# Quick Test: Cloudinary Upload

Để đảm bảo upload hoạt động, bạn có thể test theo 2 cách:

## Option 1: Test Với App (Khuyến nghị)

1. **Uninstall app cũ:**
   ```bash
   adb uninstall org.example.btvnkotlin
   ```

2. **Install app mới với logging:**
   ```bash
   adb install composeApp\build\outputs\apk\debug\composeApp-debug.apk
   ```

3. **Mở Logcat để xem logs:**
   ```bash
   adb logcat -c
   adb logcat | Select-String -Pattern "Cloudinary|Repository|ViewModel"
   ```

4. **Test trong app:**
   - Mở app → Login
   - Click + → Nhập title/description
   - Click "Chọn Ảnh" → Chọn ảnh nhỏ (< 500KB để test nhanh)
   - Click "ADD ITEM"

5. **Quan sát logs - bạn sẽ thấy một trong hai:**

   **THÀNH CÔNG:**
   ```
   🔵 Cloudinary: Starting upload...
   🔵 Cloudinary: File size: 245632 bytes
   ✅ Cloudinary: Upload SUCCESS!
   ✅ Cloudinary: URL: https://res.cloudinary.com/dwzsc2t3i/image/upload/v.../notes/image_XXX.jpg
   ```

   **THẤT BẠI:**
   ```
   🔵 Cloudinary: Starting upload...
   ❌ Cloudinary: Upload FAILED!
   ❌ Cloudinary: Error: [chi tiết lỗi]
   ```

6. **Nếu thành công:**
   - Copy URL từ log
   - Mở URL trong browser để xem ảnh
   - Quay lại HomeScreen → Ảnh nên hiển thị

7. **Nếu thất bại:**
   - Copy toàn bộ error message
   - Gửi cho tôi để debug

## Option 2: Test Cloudinary API Trực Tiếp (Curl)

Nếu app không work, test API trực tiếp:

```bash
# Tạo file test (ảnh nhỏ bất kỳ)
# Copy vào: D:\test.jpg

# Test upload với curl
curl -X POST "https://api.cloudinary.com/v1_1/dwzsc2t3i/image/upload" \
  -F "upload_preset=ml_default" \
  -F "file=@D:\test.jpg" \
  -F "folder=notes"
```

**Kết quả mong đợi:**
```json
{
  "secure_url": "https://res.cloudinary.com/dwzsc2t3i/image/upload/v.../notes/...",
  "public_id": "notes/...",
  "format": "jpg",
  ...
}
```

Nếu curl work → Vấn đề nằm ở app
Nếu curl fail → Vấn đề nằm ở Cloudinary config

## Common Issues

### 1. Upload Preset Invalid
**Error:** `Invalid upload preset`
**Fix:** 
- Vào Cloudinary → Settings → Upload Presets
- Kiểm tra "ml_default" tồn tại và là Unsigned
- Hoặc tạo preset mới, đặt tên khác (vd: "notes_unsigned")

### 2. Network Error
**Error:** `SocketTimeoutException` hoặc `UnknownHostException`
**Fix:**
- Kiểm tra internet
- Check INTERNET permission trong AndroidManifest.xml
- Test với ảnh nhỏ hơn

### 3. JSON Parsing Error
**Error:** `Serialization exception`
**Fix:**
- Cloudinary response format có thể thay đổi
- Cần update CloudinaryUploadResponse model

### 4. File Too Large
**Error:** `413 Request Entity Too Large`
**Fix:**
- Upload ảnh nhỏ hơn (< 1MB để test)
- Hoặc config Max File Size trong preset

## Cloudinary Dashboard Check

Sau khi upload (thành công hoặc fail), check:

1. **Media Library:**
   - https://console.cloudinary.com/console/media_library
   - Folder "notes/" → Ảnh có xuất hiện không?

2. **Usage:**
   - https://console.cloudinary.com/console/media_library/usage
   - Transformations/Storage có tăng không?

3. **Activity Log:**
   - Settings → Activity Log
   - Có request từ app không?

## Quick Fix: Thử Upload Preset Khác

Nếu "ml_default" không work, thử tạo preset mới:

1. Vào Cloudinary Dashboard
2. Settings → Upload → Add upload preset
3. Config:
   - **Name:** `notes_app_upload`
   - **Signing mode:** Unsigned ✅
   - **Folder:** notes
   - **Use filename:** Yes
   - **Unique filename:** Yes
4. Save

5. Update code (AdminRepository.kt):
```kotlin
private val cloudinaryService = CloudinaryService(
    cloudName = "dwzsc2t3i",
    uploadPreset = "notes_app_upload"  // <-- Đổi tên preset
)
```

6. Rebuild và test lại

## Expected Success Flow

```
User chọn ảnh
    ↓
ImagePicker returns ByteArray
    ↓
pendingFileBytes = ByteArray (có giá trị)
    ↓
Click "ADD ITEM"
    ↓
ViewModel.addNote() → uploadFileAndGetUrl()
    ↓
CloudinaryService.uploadImage()
    ↓
HTTP POST to api.cloudinary.com
    ↓
Response: { "secure_url": "https://...", ... }
    ↓
Parse JSON → CloudinaryUploadResponse
    ↓
Return URL
    ↓
Save to Firestore with URL
    ↓
HomeScreen fetches from Firestore
    ↓
KamelImage loads from Cloudinary URL
    ↓
✅ Image displays!
```

## If All Else Fails

Nếu unsigned upload không work, có thể implement signed upload (cần API Key/Secret). Nhưng **KHÔNG nên hard-code Secret trong app** - nên dùng backend/Cloud Function.

**Temporary workaround (CHỈ ĐỂ TEST):**
```kotlin
// CẢNH BÁO: Không nên dùng trong production!
private val cloudinaryService = CloudinaryService(
    cloudName = "dwzsc2t3i",
    apiKey = "933989674644269",
    apiSecret = "DtJWlM3ZVRSxuop7M1wkD7x48iU"
)
```

Nhưng cách này **RẤT KHÔNG AN TOÀN** vì Secret bị lộ trong APK.

---

Bắt đầu test với Option 1 và cho tôi biết logs bạn thấy! 🚀
