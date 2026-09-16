package com.example.myapplication1.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ============================================================
// RETROFIT CLIENT
// ============================================================

object RetrofitClient {

    private const val BASE_URL =
        "http://10.0.2.2:8082/"

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val memberApi: MemberApi by lazy {
        retrofit.create(MemberApi::class.java)
    }

    val authorApi: AuthorApi by lazy {
        retrofit.create(AuthorApi::class.java)
    }

    val categoryApi: CategoryApi by lazy {
        retrofit.create(CategoryApi::class.java)
    }

    val bookApi: BookApi by lazy {
        retrofit.create(BookApi::class.java)
    }

    val borrowApi: BorrowApi by lazy {
        retrofit.create(BorrowApi::class.java)
    }

    val borrowDetailApi: BorrowDetailApi by lazy {
        retrofit.create(BorrowDetailApi::class.java)
    }
}


// ============================================================
// AUTH REPOSITORY
// ============================================================

class AuthRepository {

    private val api = RetrofitClient.authApi

    // ========================================================
    // REGISTER
    // ========================================================

    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        password: String
    ): Result<AuthResponse> {

        return try {

            val response = api.register(
                RegisterRequest(
                    fullName = fullName.trim(),
                    email = email.trim(),
                    phone = phone.trim(),
                    password = password
                )
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(
                        Exception("Đăng ký thất bại")
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        when (response.code()) {
                            400 -> "Email đã được đăng ký hoặc thông tin không hợp lệ"
                            409 -> "Email đã được đăng ký"
                            else -> "Đăng ký thất bại (${response.code()})"
                        }
                    )
                )
            }

        } catch (e: java.io.IOException) {

            Result.failure(
                Exception(
                    "Không thể kết nối đến máy chủ"
                )
            )

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message ?: "Đăng ký thất bại"
                )
            )
        }
    }


    // ========================================================
    // LOGIN
    // ========================================================

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthResponse> {

        return try {

            val response = api.login(
                LoginRequest(
                    email = email.trim(),
                    password = password
                )
            )

            // =================================================
            // LOGIN SUCCESS - HTTP 200
            // =================================================

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            "Đăng nhập thất bại"
                        )
                    )
                }

            } else {

                // =================================================
                // LOGIN FAILED
                // =================================================

                val errorBody =
                    response.errorBody()?.string() ?: ""

                val message = when {

                    errorBody.contains(
                        "EMAIL_NOT_FOUND",
                        ignoreCase = true
                    ) ->
                        "Tài khoản không tồn tại"

                    errorBody.contains(
                        "PASSWORD_WRONG",
                        ignoreCase = true
                    ) ->
                        "Mật khẩu không đúng"

                    errorBody.contains(
                        "ACCOUNT_NOT_ACTIVE",
                        ignoreCase = true
                    ) ->
                        "Tài khoản đã bị khóa hoặc không hoạt động"

                    errorBody.contains(
                        "PASSWORD_NOT_SET",
                        ignoreCase = true
                    ) ->
                        "Tài khoản chưa có mật khẩu"

                    errorBody.contains(
                        "MEMBER_NOT_FOUND",
                        ignoreCase = true
                    ) ->
                        "Không tìm thấy thông tin thành viên"

                    response.code() == 401 ->
                        "Email hoặc mật khẩu không đúng"

                    response.code() == 404 ->
                        "Không tìm thấy máy chủ"

                    else ->
                        "Đăng nhập thất bại (${response.code()})"
                }

                Result.failure(
                    Exception(message)
                )
            }

        } catch (e: java.io.IOException) {

            // =================================================
            // SERVER CONNECTION ERROR
            // =================================================

            Result.failure(
                Exception(
                    "Không thể kết nối đến máy chủ"
                )
            )

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message ?: "Đăng nhập thất bại"
                )
            )
        }
    }
}


// ============================================================
// MEMBER REPOSITORY
// ============================================================

class MemberRepository {

    private val api = RetrofitClient.memberApi

    suspend fun getAllMembers(): List<Member> {
        return api.getAllMembers()
    }

    suspend fun getMember(
        memberId: Long
    ): Member {
        return api.getMember(memberId)
    }

    suspend fun createMember(
        request: Member
    ): Member {
        return api.createMember(request)
    }

    suspend fun updateMember(
        memberId: Long,
        fullName: String,
        email: String,
        phone: String
    ): Result<Member> {

        return try {

            val request = Member(
                memberId = memberId,
                fullName = fullName,
                email = email,
                phone = phone,
                registerDate = "",
                status = "ACTIVE"
            )

            val response =
                api.updateMember(
                    memberId,
                    request
                )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun deleteMember(
        memberId: Long
    ): Boolean {

        return try {

            val response =
                api.deleteMember(memberId)

            response.isSuccessful

        } catch (e: Exception) {

            false
        }
    }
}


// ============================================================
// AUTHOR REPOSITORY
// ============================================================

class AuthorRepository {

    private val api = RetrofitClient.authorApi

    suspend fun getAllAuthors(): List<Author> {
        return api.getAuthors()
    }

    suspend fun getAuthor(id: Long): Author {
        return api.getAuthor(id)
    }

    suspend fun createAuthor(author: Author): Author {
        return api.createAuthor(author)
    }

    suspend fun updateAuthor(
        id: Long,
        author: Author
    ): Author {
        return api.updateAuthor(id, author)
    }

    suspend fun deleteAuthor(id: Long) {
        api.deleteAuthor(id)
    }
}


// ============================================================
// CATEGORY REPOSITORY
// ============================================================

class CategoryRepository {

    private val api = RetrofitClient.categoryApi

    suspend fun getAllCategories(): List<Category> {
        return api.getCategories()
    }

    suspend fun getCategory(id: Long): Category {
        return api.getCategory(id)
    }

    suspend fun createCategory(category: Category): Category {
        return api.createCategory(category)
    }

    suspend fun updateCategory(
        id: Long,
        category: Category
    ): Category {
        return api.updateCategory(id, category)
    }

    suspend fun deleteCategory(id: Long) {
        api.deleteCategory(id)
    }
}


// ============================================================
// BOOK REPOSITORY
// ============================================================

class BookRepository {

    private val api = RetrofitClient.bookApi
    private val authorApi = RetrofitClient.authorApi
    private val categoryApi = RetrofitClient.categoryApi

    // ========================================================
    // GET ALL BOOKS
    // ========================================================

    suspend fun getAllBooks(): List<Book> {

        val response = api.getBooks()

        return response.map { book ->

            val author =
                book.authorId?.let { id ->
                    try {
                        authorApi.getAuthor(id)
                    } catch (e: Exception) {
                        null
                    }
                }

            val category =
                book.categoryId?.let { id ->
                    try {
                        categoryApi.getCategory(id)
                    } catch (e: Exception) {
                        null
                    }
                }

            Book(
                bookId = book.bookId,
                title = book.title,
                isbn = book.isbn,
                tag = book.tag,
                series = book.series,
                category = category,
                author = author,
                quantity = book.quantity,
                availableQuantity = book.availableQuantity,
                imageUrl = book.imageUrl,
                publishYear = book.publishYear,
                status = BookStatus.UNREAD
            )
        }
    }

    // ========================================================
    // GET ONE BOOK
    // ========================================================

    suspend fun getBook(id: Long): Book {

        val response = api.getBook(id)

        val author =
            response.authorId?.let {
                try {
                    authorApi.getAuthor(it)
                } catch (e: Exception) {
                    null
                }
            }

        val category =
            response.categoryId?.let {
                try {
                    categoryApi.getCategory(it)
                } catch (e: Exception) {
                    null
                }
            }

        return Book(
            bookId = response.bookId,
            title = response.title,
            isbn = response.isbn,
            tag = response.tag,
            series = response.series,
            category = category,
            author = author,
            quantity = response.quantity,
            availableQuantity = response.availableQuantity,
            imageUrl = response.imageUrl,
            publishYear = response.publishYear,
            status = BookStatus.UNREAD
        )
    }

    // ========================================================
    // CREATE BOOK
    // ========================================================

    suspend fun createBook(book: Book): Book {

        val request = BookRequest(

            // Không gửi ID khi tạo sách
            bookId = null,

            title = book.title,

            isbn = book.isbn,

            tag = book.tag,

            series = book.series,

            categoryId = book.category?.categoryId,

            authorId = book.author?.authorId,

            quantity = book.quantity,

            availableQuantity = book.availableQuantity,

            publishYear = book.publishYear,

            imageUrl = book.imageUrl
        )

        val response = api.createBook(request)

        return convertResponseToBook(response)
    }

    // ========================================================
    // UPDATE BOOK
    // ========================================================

    suspend fun updateBook(
        id: Long,
        book: Book
    ): Book {

        val request = BookRequest(

            bookId = id,

            title = book.title,

            isbn = book.isbn,

            tag = book.tag,

            series = book.series,

            categoryId = book.category?.categoryId,

            authorId = book.author?.authorId,

            quantity = book.quantity,

            availableQuantity = book.availableQuantity,

            publishYear = book.publishYear,

            imageUrl = book.imageUrl
        )

        val response =
            api.updateBook(
                id,
                request
            )

        return convertResponseToBook(response)
    }

    // ========================================================
    // DELETE BOOK
    // ========================================================

    suspend fun deleteBook(id: Long) {

        api.deleteBook(id)
    }

    // ========================================================
    // ADD BOOK FROM ADD BOOK SCREEN
    // ========================================================

    suspend fun addBook(
        title: String,
        author: String,
        imageUrl: String?,
        publishYear: Int?,
        genre: String?
    ): Result<Book> {

        return try {

            // ========================================================
            // 1. KIỂM TRA TÊN TÁC GIẢ
            // ========================================================

            val authorName = author.trim()

            if (authorName.isBlank()) {
                return Result.failure(
                    Exception("Vui lòng nhập tên tác giả")
                )
            }


            // ========================================================
            // 2. TÌM TÁC GIẢ TRONG DATABASE
            // ========================================================

            val authors = authorApi.getAuthors()

            var selectedAuthor = authors.firstOrNull {
                it.authorName.equals(
                    authorName,
                    ignoreCase = true
                )
            }


            // ========================================================
            // 3. KHÔNG CÓ TÁC GIẢ -> TẠO TÁC GIẢ MỚI
            // ========================================================

            if (selectedAuthor == null) {

                val newAuthor = Author(
                    authorId = null,
                    authorName = authorName
                )

                selectedAuthor =
                    authorApi.createAuthor(newAuthor)
            }


            // ========================================================
            // 4. TÌM CATEGORY
            // ========================================================

            val categories =
                categoryApi.getCategories()

            val selectedCategory =
                categories.firstOrNull {

                    it.categoryName.equals(
                        genre?.trim(),
                        ignoreCase = true
                    )
                }


            // ========================================================
            // 5. TẠO REQUEST SÁCH
            // ========================================================

            val request = BookRequest(

                bookId = null,

                title = title.trim(),

                isbn = null,

                tag = genre?.trim(),

                series = null,

                categoryId =
                    selectedCategory?.categoryId,

                authorId =
                    selectedAuthor.authorId,

                quantity = 1,

                availableQuantity = 1,

                publishYear = publishYear,

                imageUrl = imageUrl
            )


            // ========================================================
            // 6. POST SÁCH -> SPRING BOOT -> ORACLE
            // ========================================================

            val response =
                api.createBook(request)


            // ========================================================
            // 7. RESPONSE -> BOOK
            // ========================================================

            val book =
                convertResponseToBook(response)

            Result.success(book)

        } catch (e: java.io.IOException) {

            Result.failure(
                Exception(
                    "Không thể kết nối đến máy chủ"
                )
            )

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message ?: "Thêm sách thất bại"
                )
            )
        }
    }
    // ========================================================
    // MARK AS READ
    // ========================================================

    suspend fun markAsRead(
        bookId: Int
    ) {

        /*
         * BookStatus là trạng thái UI Android.
         *
         * LIB_BOOKS hiện tại không có cột STATUS,
         * nên không gửi READ xuống Oracle.
         *
         * Không được gọi PUT với:
         *
         * status = BookStatus.READ
         *
         * vì STATUS không tồn tại trong LIB_BOOKS.
         */
    }

    // ========================================================
    // CONVERT API RESPONSE → UI BOOK
    // ========================================================

    private suspend fun convertResponseToBook(
        response: BookResponse
    ): Book {

        val author =
            response.authorId?.let {
                try {
                    authorApi.getAuthor(it)
                } catch (e: Exception) {
                    null
                }
            }

        val category =
            response.categoryId?.let {
                try {
                    categoryApi.getCategory(it)
                } catch (e: Exception) {
                    null
                }
            }

        return Book(

            bookId = response.bookId,

            title = response.title,

            isbn = response.isbn,

            tag = response.tag,

            series = response.series,

            category = category,

            author = author,

            quantity = response.quantity,

            availableQuantity =
                response.availableQuantity,

            imageUrl = response.imageUrl,

            publishYear = response.publishYear,

            status = BookStatus.UNREAD
        )
    }
}


// ============================================================
// BORROW REPOSITORY
// ============================================================

class BorrowRepository {

    private val api = RetrofitClient.borrowApi

    suspend fun getAllBorrows(): List<Borrow> {
        return api.getBorrows()
    }

    suspend fun getBorrow(id: Long): Borrow {
        return api.getBorrow(id)
    }

    suspend fun createBorrow(borrow: Borrow): Borrow {
        return api.createBorrow(borrow)
    }

    suspend fun updateBorrow(
        id: Long,
        borrow: Borrow
    ): Borrow {
        return api.updateBorrow(id, borrow)
    }

    suspend fun deleteBorrow(id: Long) {
        api.deleteBorrow(id)
    }
}


// ============================================================
// BORROW DETAIL REPOSITORY
// ============================================================

class BorrowDetailRepository {

    private val api = RetrofitClient.borrowDetailApi

    suspend fun getAllBorrowDetails(): List<BorrowDetail> {
        return api.getBorrowDetails()
    }

    suspend fun getBorrowDetail(
        borrowId: Long,
        bookId: Long
    ): BorrowDetail {

        return api.getBorrowDetail(
            borrowId,
            bookId
        )
    }

    suspend fun createBorrowDetail(
        detail: BorrowDetail
    ): BorrowDetail {

        return api.createBorrowDetail(
            detail
        )
    }

    suspend fun updateBorrowDetail(
        borrowId: Long,
        bookId: Long,
        detail: BorrowDetail
    ): BorrowDetail {

        return api.updateBorrowDetail(
            borrowId,
            bookId,
            detail
        )
    }

    suspend fun deleteBorrowDetail(
        borrowId: Long,
        bookId: Long
    ) {

        api.deleteBorrowDetail(
            borrowId,
            bookId
        )
    }
}