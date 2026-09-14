package com.example.myapplication1.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


// =====================================================
// MEMBER API
// =====================================================

interface MemberApi {

    @GET("api/members")
    suspend fun getMembers(): Response<List<Member>>

    @GET("api/members/{id}")
    suspend fun getMember(
        @Path("id") id: Long
    ): Response<Member>

    @POST("api/members")
    suspend fun createMember(
        @Body member: Member
    ): Response<Member>

    @PUT("api/members/{id}")
    suspend fun updateMember(
        @Path("id") id: Long,
        @Body member: Member
    ): Response<Member>

    @DELETE("api/members/{id}")
    suspend fun deleteMember(
        @Path("id") id: Long
    ): Response<Void>
}


// =====================================================
// AUTHOR API
// =====================================================

interface AuthorApi {

    @GET("api/authors")
    suspend fun getAuthors(): Response<List<Author>>

    @GET("api/authors/{id}")
    suspend fun getAuthor(
        @Path("id") id: Long
    ): Response<Author>

    @POST("api/authors")
    suspend fun createAuthor(
        @Body author: Author
    ): Response<Author>

    @PUT("api/authors/{id}")
    suspend fun updateAuthor(
        @Path("id") id: Long,
        @Body author: Author
    ): Response<Author>

    @DELETE("api/authors/{id}")
    suspend fun deleteAuthor(
        @Path("id") id: Long
    ): Response<Void>
}


// =====================================================
// CATEGORY API
// =====================================================

interface CategoryApi {

    @GET("api/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("api/categories/{id}")
    suspend fun getCategory(
        @Path("id") id: Long
    ): Response<Category>

    @POST("api/categories")
    suspend fun createCategory(
        @Body category: Category
    ): Response<Category>

    @PUT("api/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body category: Category
    ): Response<Category>

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Long
    ): Response<Void>
}


// =====================================================
// BOOK API
// =====================================================

interface BookApi {

    @GET("api/books")
    suspend fun getBooks(): Response<List<Book>>

    @GET("api/books/{id}")
    suspend fun getBook(
        @Path("id") id: Long
    ): Response<Book>

    @POST("api/books")
    suspend fun createBook(
        @Body book: Book
    ): Response<Book>

    @PUT("api/books/{id}")
    suspend fun updateBook(
        @Path("id") id: Long,
        @Body book: Book
    ): Response<Book>

    @DELETE("api/books/{id}")
    suspend fun deleteBook(
        @Path("id") id: Long
    ): Response<Void>
}


// =====================================================
// BORROW API
// =====================================================

interface BorrowApi {

    @GET("api/borrows")
    suspend fun getBorrows(): Response<List<Borrow>>

    @GET("api/borrows/{id}")
    suspend fun getBorrow(
        @Path("id") id: Long
    ): Response<Borrow>

    @POST("api/borrows")
    suspend fun createBorrow(
        @Body borrow: Borrow
    ): Response<Borrow>

    @PUT("api/borrows/{id}")
    suspend fun updateBorrow(
        @Path("id") id: Long,
        @Body borrow: Borrow
    ): Response<Borrow>

    @DELETE("api/borrows/{id}")
    suspend fun deleteBorrow(
        @Path("id") id: Long
    ): Response<Void>
}


// =====================================================
// BORROW DETAIL API
// =====================================================

interface BorrowDetailApi {

    @GET("api/borrow-details")
    suspend fun getBorrowDetails(): Response<List<BorrowDetail>>

    @GET("api/borrow-details/{borrowId}/{bookId}")
    suspend fun getBorrowDetail(
        @Path("borrowId") borrowId: Long,
        @Path("bookId") bookId: Long
    ): Response<BorrowDetail>

    @POST("api/borrow-details")
    suspend fun createBorrowDetail(
        @Body detail: BorrowDetail
    ): Response<BorrowDetail>

    @PUT("api/borrow-details/{borrowId}/{bookId}")
    suspend fun updateBorrowDetail(
        @Path("borrowId") borrowId: Long,
        @Path("bookId") bookId: Long,
        @Body detail: BorrowDetail
    ): Response<BorrowDetail>

    @DELETE("api/borrow-details/{borrowId}/{bookId}")
    suspend fun deleteBorrowDetail(
        @Path("borrowId") borrowId: Long,
        @Path("bookId") bookId: Long
    ): Response<Void>
}


// =====================================================
// RETROFIT CLIENT
// =====================================================

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


// =====================================================
// MEMBER REPOSITORY
// =====================================================

class MemberRepository {

    suspend fun getMembers(): Result<List<Member>> {
        return try {
            val response = RetrofitClient.memberApi.getMembers()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("HTTP ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMember(id: Long): Result<Member> {
        return try {
            val response = RetrofitClient.memberApi.getMember(id)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("HTTP ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMember(member: Member): Result<Member> {
        return try {
            val response =
                RetrofitClient.memberApi.createMember(member)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("HTTP ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMember(
        id: Long,
        member: Member
    ): Result<Member> {
        return try {
            val response =
                RetrofitClient.memberApi.updateMember(id, member)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("HTTP ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMember(id: Long): Result<Unit> {
        return try {
            val response =
                RetrofitClient.memberApi.deleteMember(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("HTTP ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// =====================================================
// AUTHOR REPOSITORY
// =====================================================

class AuthorRepository {

    suspend fun getAuthors(): Result<List<Author>> {
        return try {
            val response = RetrofitClient.authorApi.getAuthors()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAuthor(author: Author): Result<Author> {
        return try {
            val response =
                RetrofitClient.authorApi.createAuthor(author)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAuthor(
        id: Long,
        author: Author
    ): Result<Author> {
        return try {
            val response =
                RetrofitClient.authorApi.updateAuthor(id, author)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAuthor(id: Long): Result<Unit> {
        return try {
            val response =
                RetrofitClient.authorApi.deleteAuthor(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// =====================================================
// CATEGORY REPOSITORY
// =====================================================

class CategoryRepository {

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response =
                RetrofitClient.categoryApi.getCategories()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCategory(
        category: Category
    ): Result<Category> {
        return try {
            val response =
                RetrofitClient.categoryApi
                    .createCategory(category)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCategory(
        id: Long,
        category: Category
    ): Result<Category> {
        return try {
            val response =
                RetrofitClient.categoryApi
                    .updateCategory(id, category)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCategory(id: Long): Result<Unit> {
        return try {
            val response =
                RetrofitClient.categoryApi.deleteCategory(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// =====================================================
// BOOK REPOSITORY
// =====================================================

class BookRepository {

    suspend fun getBooks(): Result<List<Book>> {
        return try {
            val response = RetrofitClient.bookApi.getBooks()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBook(book: Book): Result<Book> {
        return try {
            val response =
                RetrofitClient.bookApi.createBook(book)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBook(
        id: Long,
        book: Book
    ): Result<Book> {
        return try {
            val response =
                RetrofitClient.bookApi.updateBook(id, book)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBook(id: Long): Result<Unit> {
        return try {
            val response =
                RetrofitClient.bookApi.deleteBook(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// =====================================================
// BORROW REPOSITORY
// =====================================================

class BorrowRepository {

    suspend fun getBorrows(): Result<List<Borrow>> {
        return try {
            val response =
                RetrofitClient.borrowApi.getBorrows()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBorrow(
        borrow: Borrow
    ): Result<Borrow> {
        return try {
            val response =
                RetrofitClient.borrowApi.createBorrow(borrow)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBorrow(
        id: Long,
        borrow: Borrow
    ): Result<Borrow> {
        return try {
            val response =
                RetrofitClient.borrowApi.updateBorrow(id, borrow)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBorrow(id: Long): Result<Unit> {
        return try {
            val response =
                RetrofitClient.borrowApi.deleteBorrow(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// =====================================================
// BORROW DETAIL REPOSITORY
// =====================================================

class BorrowDetailRepository {

    suspend fun getBorrowDetails(): Result<List<BorrowDetail>> {
        return try {
            val response =
                RetrofitClient.borrowDetailApi
                    .getBorrowDetails()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBorrowDetail(
        detail: BorrowDetail
    ): Result<BorrowDetail> {
        return try {
            val response =
                RetrofitClient.borrowDetailApi
                    .createBorrowDetail(detail)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBorrowDetail(
        borrowId: Long,
        bookId: Long,
        detail: BorrowDetail
    ): Result<BorrowDetail> {
        return try {
            val response =
                RetrofitClient.borrowDetailApi
                    .updateBorrowDetail(
                        borrowId,
                        bookId,
                        detail
                    )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBorrowDetail(
        borrowId: Long,
        bookId: Long
    ): Result<Unit> {
        return try {
            val response =
                RetrofitClient.borrowDetailApi
                    .deleteBorrowDetail(
                        borrowId,
                        bookId
                    )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}