# Tóm tắt kiến trúc & vai trò các thành phần ứng dụng **Shop Management**

---

## Kiến trúc tổng thể

Ứng dụng xây dựng theo **mô hình phân lớp (Layered Architecture)** gồm các lớp chính:

-   **Entity (models):** Đại diện cho các đối tượng dữ liệu trong hệ thống.
-   **Repository:** Quản lý lưu trữ, truy xuất và thao tác dữ liệu.
-   **Service:** Xử lý nghiệp vụ, logic ứng dụng.
-   **Utils:** Hỗ trợ các chức năng tiện ích như giao diện dòng lệnh, truy vấn dữ liệu.
-   **Main:** Điểm khởi động ứng dụng, điều phối luồng hoạt động.

---

## 1. ENTITY (models)

**Vai trò lớp:** Định nghĩa các đối tượng dữ liệu:

-   **Product:** Sản phẩm trong cửa hàng.
-   **Category:** Nhóm sản phẩm.
-   **User:** Người dùng (thông tin đăng nhập, vai trò).
-   **Entity:** Lớp cha, cung cấp thuộc tính chung (`id`, `name`, `createdAt`).

---

## 2. REPOSITORY (`Repository.java`, repositories)

**Vai trò lớp:** Quản lý lưu trữ, truy xuất, cập nhật, xóa dữ liệu cho từng loại entity.

**Các đối tượng chính:**

-   **`Repository.java`:**  
    Lớp trừu tượng tổng quát, cung cấp các phương thức thao tác dữ liệu chung như thêm, cập nhật, xóa, tìm kiếm, kiểm tra trùng lặp, sắp xếp, quản lý dung lượng mảng dữ liệu.  
    Được kế thừa bởi các repository cụ thể cho từng entity.

-   **`UserRepository.java`:**  
    Quản lý dữ liệu người dùng.  
    Cung cấp các phương thức đặc thù như tìm kiếm theo email (`findByEmail`), kiểm tra tồn tại email (`existsByEmail`).  
    Đảm bảo mỗi email là duy nhất trong hệ thống, hỗ trợ xác thực và đăng nhập.

-   **`ProductRepository.java`:**  
    Quản lý dữ liệu sản phẩm.  
    Bổ sung các phương thức sắp xếp sản phẩm theo giá (`sortByPrice`) và số lượng (`sortByQuantity`), hỗ trợ truy vấn nâng cao cho chức năng quản lý kho và hiển thị sản phẩm.

-   **`CategoryRepository.java`:**  
    Quản lý dữ liệu nhóm sản phẩm.  
    Đơn giản hóa thao tác thêm, sửa, xóa, tìm kiếm nhóm sản phẩm, hỗ trợ phân loại sản phẩm trong cửa hàng.

**Singleton pattern:**  
Mỗi repository đều sử dụng singleton để đảm bảo chỉ có một thể hiện duy nhất quản lý dữ liệu từng loại entity trong toàn bộ ứng dụng.

**Xử lý lỗi:**  
Kiểm tra dữ liệu đầu vào, ném các exception như:

-   `BadRequestException`
-   `NotFoundException`
-   `InternalAppException`

---

## 3. SERVICE (services)

**Vai trò lớp:** Xử lý logic nghiệp vụ, xác thực, quản lý phiên, thao tác sản phẩm.

**Các interface & class chính:**

-   **`IAuthService.java`:**  
    Định nghĩa các phương thức xác thực người dùng:

    -   `login(email, password)`: Đăng nhập, trả về đối tượng User nếu thành công.
    -   `register(email, password, checkPassword, name)`: Đăng ký tài khoản mới.
    -   `logout(uid)`: Đăng xuất, hủy phiên.
    -   `validSession(uid)`: Kiểm tra phiên đăng nhập còn hiệu lực.
    -   `isAdmin(uid)`: Kiểm tra quyền admin của người dùng.

-   **`AuthService.java`:**  
    Triển khai nghiệp vụ xác thực, đăng nhập, đăng ký, đăng xuất, kiểm tra quyền admin.  
    Kết nối với `UserRepository` để truy xuất dữ liệu người dùng và với `SessionService` để quản lý phiên.  
    Kiểm tra hợp lệ dữ liệu đầu vào, xử lý lỗi, đảm bảo bảo mật thông tin đăng nhập.

-   **`ISessionService.java`:**  
    Định nghĩa các phương thức quản lý phiên đăng nhập:

    -   `create(userId)`: Tạo phiên mới khi đăng nhập.
    -   `remove(userId)`: Xóa phiên khi đăng xuất.
    -   `isActive(userId)`: Kiểm tra phiên còn hiệu lực.
    -   `getRemainingSeconds(userId)`: Thời gian còn lại của phiên.

-   **`SessionService.java`:**  
    Quản lý phiên đăng nhập bằng cách lưu thời điểm đăng nhập của từng user.  
    Kiểm tra timeout phiên, tự động hủy phiên khi hết hạn.  
    Đảm bảo mỗi user chỉ có một phiên duy nhất đang hoạt động.

-   **`IProductService.java`:**  
    Định nghĩa các phương thức quản lý sản phẩm:

    -   `getById(id)`: Lấy sản phẩm theo ID.
    -   `add(data)`, `update(data)`, `delete(id)`: Thêm, cập nhật, xóa sản phẩm.
    -   `getList(page, limit, sortBy, order)`: Lấy danh sách sản phẩm có phân trang và sắp xếp.
    -   `search(keyword, searchBy, ...)`: Tìm kiếm sản phẩm theo trường cụ thể, có phân trang và sắp xếp.
    -   `deleteAll()`: Xóa toàn bộ sản phẩm.

-   **`ProductService.java`:**  
    Triển khai nghiệp vụ quản lý sản phẩm, kết nối với `ProductRepository`.  
    Xử lý logic tìm kiếm, phân trang, sắp xếp, kiểm tra hợp lệ dữ liệu sản phẩm.  
    Đảm bảo thao tác thêm/xóa/cập nhật sản phẩm an toàn, kiểm soát lỗi rõ ràng.

**Xử lý lỗi:**  
Kiểm tra hợp lệ dữ liệu, xác thực quyền, ném exception khi thao tác không hợp lệ.

---

## 4. UTILS (`GUI.java`, `Query.java`)

**Vai trò lớp:**

-   **GUI:** Quản lý giao diện dòng lệnh, nhập xuất, hiển thị menu, bảng sản phẩm, xác thực quyền thao tác.
-   **Query:** Hỗ trợ thao tác dữ liệu dạng mảng: lọc, sắp xếp, phân trang, tìm kiếm.

**Xử lý lỗi:** Hiển thị thông báo lỗi cho người dùng, kiểm tra quyền trước khi thao tác.

---

## 5. MAIN (`Main.java`)

**Vai trò lớp:** Điểm khởi động ứng dụng, vòng lặp chính, nhận lệnh từ người dùng, điều phối các chức năng.

**Các đối tượng chính:** Khởi tạo dữ liệu mẫu, quản lý trạng thái đăng nhập, gọi các chức năng qua GUI.

**Xử lý lỗi:** Bắt và hiển thị lỗi trong quá trình chạy, đảm bảo ứng dụng không bị crash.

---

## Tổng kết xử lý lỗi

-   Sử dụng các lớp exception chuyên biệt (`AppException`, `BadRequestException`, `NotFoundException`, `InternalAppException`) để kiểm soát và thông báo lỗi rõ ràng.
-   Kiểm tra dữ liệu đầu vào, quyền truy cập, trạng thái phiên trước khi thực hiện thao tác.

---

## Tóm tắt vai trò từng lớp

| Lớp            | Vai trò chính                                 |
| -------------- | --------------------------------------------- |
| **Entity**     | Định nghĩa dữ liệu                            |
| **Repository** | Quản lý lưu trữ, truy xuất dữ liệu            |
| **Service**    | Xử lý nghiệp vụ, xác thực, quản lý phiên      |
| **Utils**      | Hỗ trợ giao diện và thao tác dữ liệu          |
| **Main**       | Điều phối hoạt động, giao tiếp với người dùng |
