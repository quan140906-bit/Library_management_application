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
        return api.getMembers()
    }

    suspend fun getMember(id: Long): Member {
        return api.getMember(id)
    }

    suspend fun createMember(member: Member): Member {
        return api.createMember(member)
    }

    suspend fun updateMember(
        id: Long,
        member: Member
    ): Member {
        return api.updateMember(id, member)
    }

    suspend fun deleteMember(id: Long) {
        api.deleteMember(id)
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

    suspend fun getAllBooks(): List<Book> {
        return api.getBooks()
    }

    suspend fun getBook(id: Long): Book {
        return api.getBook(id)
    }

    suspend fun createBook(book: Book): Book {
        return api.createBook(book)
    }

    suspend fun updateBook(
        id: Long,
        book: Book
    ): Book {
        return api.updateBook(id, book)
    }

    suspend fun deleteBook(id: Long) {
        api.deleteBook(id)
    }

    suspend fun addBook(
        title: String,
        author: String,
        imageUrl: String?,
        publishYear: Int?,
        genre: String?
    ) {

        val book = Book(
            bookId = 0L,
            title = title,
            isbn = null,
            tag = null,
            series = null,
            category = null,
            author = Author(
                0L,
                author
            ),
            quantity = 1,
            availableQuantity = 1,
            imageUrl = imageUrl,
            status = BookStatus.READING
        )

        try {

            api.createBook(book)

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    suspend fun markAsRead(
        bookId: Int
    ) {

        try {

            val book =
                api.getBook(bookId.toLong())

            api.updateBook(
                bookId.toLong(),
                book.copy(
                    status = BookStatus.READ
                )
            )

        } catch (e: Exception) {

            e.printStackTrace()
        }
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