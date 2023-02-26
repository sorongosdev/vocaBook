package com.sorongos.vocabook

import androidx.room.Entity
import androidx.room.PrimaryKey

/**data class : 데이터를 홀딩하기 위한 클래스
 * 자바는 없음
 * 상속 불가
 * 테이블을 사용하기 위해 어노테이션
 * */

@Entity(tableName = "word") //table 이름 지정
data class Word(
    val text: String,
    val mean: String,
    val type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0 // unique한 키값을 설정을 위해, 자동 아이디 설정
)
