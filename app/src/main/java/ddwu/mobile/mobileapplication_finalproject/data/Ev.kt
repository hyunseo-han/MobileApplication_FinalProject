package ddwu.mobile.mobileapplication_finalproject.data

data class EVRoot(
    val currentCount: Long,
    val data: List<EvDetail>,
    val matchCount: Long,
    val page: Long,
    val perPage: Long,
    val totalCount: Long,
)

data class EvDetail(
    val addr: String, //주소
    val chargeTp: String, //
    val cpId: Long, //충전기 ID
    val cpNm: String, //충전기 명칭
    val cpStat: String, //충전기 상태 0:상태확인불가, 1: 충전가능, 2: 충전중, 3: 고장...
    val cpTp: String, //충전방식
    val csId: Long, //충전소ID
    val csNm: String, //충전소 명칭
    val lat: String, //위도
    val longi: String, //경도
    val statUpdatetime: String, //충전기 상태 갱신 시각
)