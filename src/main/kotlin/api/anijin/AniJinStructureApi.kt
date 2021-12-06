package api.anijin

interface AniJinStructureApi<T> {
    suspend fun get(): Result<T>
}