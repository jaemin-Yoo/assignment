package com.jaemin.assignment

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.jaemin.assignment.data.UnsplashRepository
import com.jaemin.assignment.factory.UnsplashPhotoFactory
import com.jaemin.assignment.ui.feed.FeedViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var unsplashRepository: UnsplashRepository

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { unsplashRepository.favoritePhoto } returns emptyFlow()
        viewModel = FeedViewModel(unsplashRepository)
    }

    @Test
    fun `이미지 검색 시 Loading 상태가 되는가`() = runTest {
        // given
        val photos = UnsplashPhotoFactory.createUnsplashPhotos(3)
        val pagingData = PagingData.from(photos)
        every { unsplashRepository.getUnsplashPhotosStream(any()) } returns flowOf(pagingData)

        // when
        viewModel.search()
        val result = viewModel.feedUiState.value

        // then
        assertThat(result).isInstanceOf(FeedViewModel.FeedUiState.Loading::class.java)
    }

    @Test
    fun `이미지 리스트를 성공적으로 가져오면 Success 상태가 되는가`() = runTest {
        // given
        val photos = UnsplashPhotoFactory.createUnsplashPhotos(3)
        val pagingData = PagingData.from(photos)
        every { unsplashRepository.getUnsplashPhotosStream(any()) } returns flowOf(pagingData)

        // when
        viewModel.search()
        delay(1000)
        val result = viewModel.feedUiState.value

        // then
        assertThat(result).isInstanceOf(FeedViewModel.FeedUiState.Success::class.java)
    }
}