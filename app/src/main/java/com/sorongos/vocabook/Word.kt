package com.sorongos.vocabook

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**data class : 데이터를 홀딩하기 위한 클래스
 * 자바는 없음
 * 상속 불가
 * 테이블을 사용하기 위해 어노테이션
 *
 * Parcel : 꾸러미
 * Parcelable interface는 Parcel을 만들고 비직렬화(풀어주기)를 위한 인터페이스이다.
 * Serializable과 다르게 리플렉션을 사용하지 않는다.
 * 필요한 부분만 직렬화를 한다.
 * */

@Parcelize
@Entity(tableName = "word") //table 이름 지정
data class Word(
    val text: String,
    val mean: String,
    val type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0 // unique한 키값을 설정을 위해, 자동 아이디 설정
) : Parcelable
