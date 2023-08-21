package com.ch4vi.topRatedShows.featureTVShows.data.api.datasource

import com.ch4vi.topRatedShows.featureTVShows.data.api.model.PageDTO

object ApiConstants {
    val tvShowPageSuccessResponse by lazy {
        """
        {
            "page": 1,
            "total_pages": 10,
            "total_results": 100,
            "results": [
            {
                "id": 1,
                "name": "foo",
                "poster_path": "foo.jpg"
            },
            {
                "id": 2,
                "name": "bar",
                "poster_path": "bar.jpg"
            }
            ]
        }
        """
    }

    val tvShowPageErrorResponse by lazy {
        """
        {
            "total_results": 100,
            "results": [
            {
                "id": 1,
                "name": "foo",
                "poster_path": "foo.jpg"
            },
            {
                "id": 2,
                "name": "bar",
                "poster_path": "bar.jpg"
            }
            ]
        }
        """
    }

    val tvShowItemSuccessResponse by lazy {
        """
        {
            "id": 1,
            "name": "foo",
            "poster_path": "foo.jpg"
        }
        """
    }

    val tvShowItemErrorResponse by lazy {
        """
        {
            "name": "foo",
            "poster_path": "foo.jpg"
        }
        """
    }

    val pageDTO by lazy {
        PageDTO(
            pageIndex = 1,
            totalPages = 10,
            items = listOf()
        )
    }
}
