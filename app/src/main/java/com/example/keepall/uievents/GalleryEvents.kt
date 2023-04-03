package com.example.keepall.uievents

sealed class GalleryEvents {
    object Show: GalleryEvents()
    object Hide: GalleryEvents()
}
