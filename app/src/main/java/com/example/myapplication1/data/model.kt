package com.example.myapplication1.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// ============================================================
// AUTH
// ============================================================

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val accountId: Long,
    val memberId: Long,
    val fullName: String,
    val email: String,
    val role: String,
    val status: String
)

data class ErrorResponse(
    val message: String?
)


// ============================================================
// MEMBER
// ============================================================

data class Member(
    val memberId: Long,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val registerDate: String,
    val status: String
)


// ============================================================
// AUTHOR
// ============================================================

data class Author(
    val authorId: Long,
    val authorName: String
)


// ============================================================
// CATEGORY
// ============================================================

data class Category(
    val categoryId: Long,
    val categoryName: String
)


// ============================================================
// BOOK
// ============================================================

enum class BookStatus {
    READ, READING, UNREAD
}

data class Book(
    val bookId: Long,
    val title: String,
    val isbn: String?,
    val tag: String?,
    val series: String?,
    val category: Category?,
    val author: Author?,
    val quantity: Int,
    val availableQuantity: Int,
    val imageUrl: String? = null,
    val status: BookStatus = BookStatus.UNREAD
)


// ============================================================
// BORROW
// ============================================================

data class Borrow(
    val borrowId: Long,
    val member: Member,
    val borrowDate: String,
    val dueDate: String,
    val status: String
)


// ============================================================
// BORROW DETAIL
// ============================================================

data class BorrowDetail(
    val borrowId: Long,
    val bookId: Long,
    val borrow: Borrow?,
    val book: Book?,
    val returnDate: String?
)


// ============================================================
// AUTH API
// ============================================================

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): retrofit2.Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): retrofit2.Response<AuthResponse>
}


// ============================================================
// MEMBER API
// ============================================================

interface MemberApi {

    @GET("api/members")
    suspend fun getMembers(): List<Member>

    @GET("api/members/{id}")
    suspend fun getMember(
        @Path("id") id: Long
    ): Member

    @POST("api/members")
    suspend fun createMember(
        @Body member: Member
    ): Member

    @PUT("api/members/{id}")
    suspend fun updateMember(
        @Path("id") id: Long,
        @Body member: Member
    ): Member

    @DELETE("api/members/{id}")
    suspend fun deleteMember(
        @Path("id") id: Long
    )
}


// ============================================================
// AUTHOR API
// ============================================================

interface AuthorApi {

    @GET("api/authors")
    suspend fun getAuthors(): List<Author>

    @GET("api/authors/{id}")
    suspend fun getAuthor(
        @Path("id") id: Long
    ): Author

    @POST("api/authors")
    suspend fun createAuthor(
        @Body author: Author
    ): Author

    @PUT("api/authors/{id}")
    suspend fun updateAuthor(
        @Path("id") id: Long,
        @Body author: Author
    ): Author

    @DELETE("api/authors/{id}")
    suspend fun deleteAuthor(
        @Path("id") id: Long
    )
}


// ============================================================
// CATEGORY API
// ============================================================

interface CategoryApi {

    @GET("api/categories")
    suspend fun getCategories(): List<Category>

    @GET("api/categories/{id}")
    suspend fun getCategory(
        @Path("id") id: Long
    ): Category

    @POST("api/categories")
    suspend fun createCategory(
        @Body category: Category
    ): Category

    @PUT("api/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body category: Category
    ): Category

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Long
    )
}


// ============================================================
// BOOK API
// ============================================================

interface BookApi {

    @GET("api/books")
    suspend fun getBooks(): List<Book>

    @GET("api/books/{id}")
    suspend fun getBook(
        @Path("id") id: Long
    ): Book

    @POST("api/books")
    suspend fun createBook(
        @Body book: Book
    ): Book

    @PUT("api/books/{id}")
    suspend fun updateBook(
        @Path("id") id: Long,
        @Body book: Book
    ): Book

    @DELETE("api/books/{id}")
    suspend fun deleteBook(
        @Path("id") id: Long
    )
}


// ============================================================
// BORROW API
// ============================================================

interface BorrowApi {

    @GET("api/borrows")
    suspend fun getBorrows(): List<Borrow>

    @GET("api/borrows/{id}")
    suspend fun getBorrow(
        @Path("id") id: Long
    ): Borrow

    @POST("api/borrows")
    suspend fun createBorrow(
        @Body borrow: Borrow
    ): Borrow

    @PUT("api/borrows/{id}")
    suspend fun updateBorrow(
        @Path("id") id: Long,
        @Body borrow: Borrow
    ): Borrow

    @DELETE("api/borrows/{id}")
    suspend fun deleteBorrow(
        @Path("id") id: Long
    )
}


// ============================================================
// BORROW DETAIL API
// ============================================================

interface BorrowDetailApi {

    @GET("api/borrow-details")
    suspend fun getBorrowDetails(): List<BorrowDetail>

    @GET("api/borrow-details/{borrowId}/{bookId}")
    suspend fun getBorrowDetail(
        @Path("borrowId") borrowId: Long,
        @Path("bookId") bookId: Long
    ): BorrowDetail

    @POST("api/borrow-details")
    suspend fun createBorrowDetail(
        @Body detail: BorrowDetail
    ): BorrowDetail

    @PUT("api/borrow-details/{borrowId}/{bookId}")
    suspend fun updateBorrowDetail(
        @Path("borrowId") borrowId: Long,
        @Path("bookId") bookId: Long,
        @Body detail: BorrowDetail
    ): BorrowDetail

    @DELETE("api/borrow-details/{borrowId}/{bookId}")
    suspend fun deleteBorrowDetail(
        @Path("borrowId") borrowId: Long,
        @Path("bookId") bookId: Long
    )
}