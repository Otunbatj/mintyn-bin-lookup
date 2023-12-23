package com.testmintyn.binlookup.service.impl

import org.springframework.stereotype.Service


@Service
class HitCounterService {
    private val binMap = mutableMapOf<String, Int>()

    /**
     * @param bin is the first 6 digits of a card number as received from the customer
     * This method receives a particular bin and saves it into the map
     * It assigns a value of 1 for the first hit and gradually increments by 1
     * for every hit
     * @return Int value of the number of times a card bin has been queried on the system
     */
    fun incrementCounter(bin: String): Int? {
        binMap[bin] = getSingleBinHits(bin) + 1
        return binMap[bin]
    }

    /**
     * @param bin is the first 6 digits of a card number as received from the customer
     * This method receives a bin and returns the number of hits that has been received for that particular bin
     */
    fun getSingleBinHits(bin: String): Int {
        return binMap.getOrDefault(bin, 0)
    }

    /**
     * This returns the data containing all the bin hits that has been received in the system
     * Can easily be converted to JSON key value pairs
     */
    fun getBinStats(): Map<String, Int> {
        return binMap
    }

    /**
     * This method Paginates the local cached data of BIN hits and
     * returns a paginated stats of BIN hit
     */
    fun getPaginatedBINStarts(page: Int, limit: Int): Map<String, Int> {
        //Deserialize the BIN in the system into entries of key value pairs
        //This is to be able to fetch each entry's by index
        val entryList: List<Map.Entry<String, Int>> = ArrayList(binMap.entries)
        val customBinMap = mutableMapOf<String, Int>()
        //Get the start index which is the page number multiplied by the limit minus 1
        val start = (page - 1) * limit
        //Get the start index which is the page number multiplied by the limit
        val end = start + limit
        val startIndex = if(start > 0) start - 1 else start
        val endIndex = end - 1

        for (i in startIndex until endIndex.coerceAtMost(binMap.size)) {
            if (i >= 0 && i < entryList.size) {
                val entry = entryList[i]
                customBinMap[entry.key] = entry.value
            }
        }
        return customBinMap
    }

    fun getTotalSize(): Int {
        return binMap.size
    }
}