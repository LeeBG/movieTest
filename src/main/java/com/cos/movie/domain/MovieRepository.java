package com.cos.movie.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

// 더미데이터 넣기
@Repository
public class MovieRepository {
	
	//전체 영화 가져오기
	public List<Movie> findAll(){
		List<Movie> movies = new ArrayList<>();
		movies.add(new Movie(1,9.1,"소울",null));
		movies.add(new Movie(2,5.8,"극장판 귀멸의 칼날 무한열차편",null));
		movies.add(new Movie(3,8.4,"아이엠 우먼",null));
		movies.add(new Movie(4,10.0,"파힘",null));
		movies.add(new Movie(5,7.1,"더 시크릿",null));
		movies.add(new Movie(6,8.3,"모추어리 컬렉션",null));
		movies.add(new Movie(7,6.8,"테넷",null));
		movies.add(new Movie(8,8.9,"마이 미씽 발렌타인",null));
		return movies;
	}
	//영화 1건 가져오기
	public Movie findById(int id){		//영화 한건 가져오기
		System.out.println(id+"번 영화 select");
		return new Movie(id,9.1,"소울",null);
	}
	
	//영화 등록하기
	public Movie save(JoinReqDto dto) {
		if(dto.getTitle()!=null) {
			System.out.println("영화 제목 : "+dto.getTitle()+"가 추가되었습니다.");
			return new Movie(9,dto.getRating(),dto.getTitle(),null);
		}
		return null;
	}
	
	// 영화 삭제하기
	public void delete(int id) {
		System.out.println("id: "+id+"번 영화 삭제하기");
	}
	
	//영화 수정하기
	public Movie update(int id, UpdateReqDto dto) {
		System.out.println(id+"번 영화 :"+dto.getTitle()+"평점:"+dto.getRating()+"\n 수정 하기");
		return new Movie(id,dto.getRating(),dto.getTitle(),null);
	}
}
