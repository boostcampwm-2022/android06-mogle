package com.wakeup.data.source.local.moment

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.toDomain
import com.wakeup.domain.model.Moment
import kotlinx.coroutines.flow.last

private const val STARTING_PAGE_INDEX = 1

class MomentLocalPagingSource(
    private val localDataSource: MomentLocalDataSource,
    private val query: String,
    private val sort: String
) : PagingSource<Int, Moment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Moment> {
        val position = params.key ?: STARTING_PAGE_INDEX

        // TODO: 수정 필요
        val response = listOf(
            Moment(
                1L,
                "흰여울문화마을",
                "부산 영도구 영선동4가 605-3",
                null,
                "흰여울 문화마을에 갔다. 너무 재밌었다. 바다가 너무 이뻤다. 동굴에서 사람들 사진찍는거 구경도 하고 등산도 하고 현지인이랑 대화도 했다. 뜻깊은 하루였다.",
                listOf("default"),
                "2022-11-18"
            )
        )

        /*val response = momentDao.getMoments().map { moment ->
            moment.toDomain(
                momentDao.getPictures(moment.id).last(),
                momentDao.getGlobes(moment.id).last(),
            )
        }*/

        return try {
            toLoadResult(response, position)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Moment>): Int? = state.anchorPosition

    private fun toLoadResult(
        response: List<Moment>,
        position: Int
    ): LoadResult<Int, Moment> {
        return LoadResult.Page(
            data = response,
            prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
            nextKey = if (response.isEmpty()) null else position + 1
        )
    }
}