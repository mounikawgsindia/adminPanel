package com.wingspan.shopkeeper.api

import android.util.LruCache

object ApiDataCache {
    private val cacheSize = 10 * 1024 * 1024 // 10 mb data

    // LRU Cache to hold API data, the key is a String and value can be of Any type
    val lruCache: LruCache<String, Any> = LruCache(cacheSize)

    // Define all cache keys as constants
    object CacheKeys {
        const val APPROVED_LIST = "approved_list-cache"
        const val REJECTED_LIST = "rejected_list-cache"
        const val PENDING_LIST = "pending_list-cache"
        // Add more keys as needed
    }

    // Put data into cache
    fun put(key: String, value: Any) {
        lruCache.put(getCacheKey(key), value)
    }

    // Get data from cache
    fun get(key: String): Any? {
        return lruCache.get(getCacheKey(key))
    }
    //remove cache
    // ApiDataCache.remove(ApiDataCache.CacheKeys.FLASH_SALES)
    // Remove data from cache
    fun remove(key: String) {
        lruCache.remove(getCacheKey(key))
    }
    // Clear all cache entries
    fun clear() {
        lruCache.evictAll()
    }
    // Generate a unique key for cache entries
    fun getCacheKey(apiName: String): String {
        return apiName
    }
}