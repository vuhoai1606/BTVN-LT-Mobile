package Bai3

fun main() {
    // --- ĐỐI TƯỢNG COMPANION (Truy cập hằng số không cần tạo object) ---
    println("Hãng sản xuất: ${DieuKhienDen.HANG_SAN_XUAT}")
    println("Phiên bản Firmware: ${DieuKhienDen.VERSION}")
    println("\n----------------\n")

    // --- KHỞI TẠO TRỄ (LATEINIT) ---
    val denPhongKhach = DieuKhienDen()

    // Nếu in biến 'tenThietBi' ngay lúc này sẽ bị lỗi vì chưa gán (UninitializedPropertyAccessException)
    // Gán giá trị sau khi khởi tạo
    denPhongKhach.datTen("Đèn Chùm Pha Lê")
    denPhongKhach.inThongTin()
    println("\n----------------\n")

    // --- THUỘC TÍNH DỰ PHÒNG ---
    // Thay đổi độ sáng thông qua hàm setter nội bộ
    denPhongKhach.tangDoSang()
    println("Độ sáng hiện tại: ${denPhongKhach.doSangHienThi}%")
    println("\n----------------\n")


    // --- HÀM LAMBDA ---
    val tinhTienDien: (Int) -> Int = { soGio: Int -> soGio * 2000 }

    val soGioDung = 5
    println("Dùng $soGioDung giờ hết: ${tinhTienDien(soGioDung)} VNĐ")
    println("\n----------------\n")


    // --- TOÁN TỬ ELVIS (?:) & LỆNH GỌI AN TOÀN (?.) ---
    // Giả lập dữ liệu nhận từ Server về (có thể bị null)
    var duLieuServer: String? = null

    // 1. Lệnh gọi an toàn (?.): Nếu null thì bỏ qua, không crash
    val doDaiChuoi = duLieuServer?.length
    println("Độ dài dữ liệu (Safe call): $doDaiChuoi") // In ra: null

    // 2. Toán tử Elvis (?:): Nếu null thì lấy giá trị mặc định
    // Nếu duLieuServer là null thì lấy chuỗi "Mất kết nối", ngược lại lấy giá trị thật
    val trangThaiMang = duLieuServer ?: "Mất kết nối Internet"
    println("Trạng thái mạng (Elvis): $trangThaiMang")
    println("\n----------------\n")


    // --- ỦY QUYỀN THUỘC TÍNH ---
    // Khi gọi đến biến này, nó mới bắt đầu chạy code khởi tạo (Lazy)
    println("Chuẩn bị kết nối Hub tổng...")
    println("Trạng thái Hub: ${denPhongKhach.ketNoiHub}")
}

class DieuKhienDen {

    // Đối tượng Companion (Giống static trong Java)
    companion object {
        const val HANG_SAN_XUAT = "Xiaomi"
        const val VERSION = 2.0
    }

    // Khởi tạo trễ (lateinit)
    // Khai báo biến non-null nhưng chưa cần gán giá trị ngay lập tức
    lateinit var tenThietBi: String

    fun datTen(ten: String) {
        tenThietBi = ten
    }

    fun inThongTin() {
        // Kiểm tra xem đã khởi tạo chưa
        if (this::tenThietBi.isInitialized) {
            println("Thiết bị đang hoạt động: $tenThietBi")
        }
    }

    // Thuộc tính dự phòng
    // _doSang: Biến nội bộ, có thể chỉnh sửa (private var)
    private var _doSang = 50

    // doSangHienThi: Biến public, chỉ cho phép đọc (val), lấy giá trị từ _doSang
    val doSangHienThi: Int
        get() = _doSang

    fun tangDoSang() {
        _doSang += 10
        println("Đã tăng độ sáng lên $_doSang%")
    }

    // Ủy quyền thuộc tính
    // 'by lazy': Chỉ thực thi đoạn code trong ngoặc nhọn KHI VÀ CHỈ KHI biến này được gọi lần đầu tiên.
    val ketNoiHub: String by lazy {
        println("...Đang thực hiện kết nối tốn thời gian...")
        "Đã kết nối thành công (Connected)"
    }
}