# movieTest

## 2021/01/19 인터페이스 구현실기시험

### [평가] 다음과 같은 애플리케이션 서버를 구축하라.
 
 1. host주소 = http://localhost:8080

 2. 요청과 응답
 - 요청 Type 공통 = application/json
 - 응답 Type 공통 = application/json

 3. 전체영화 가져오기
 - GET    /movie
 - 응답 BODY 데이터 (Movie 오브젝트를 컬렉션에 담아 json으로 변환하여 응답)


 4. 영화 1건 가져오기(숫자로 구분 = 숫자는 영화의 id값)
 - Get    /movie/8
 - 응답 BODY 데이터 (Movie 오브젝트를 json으로 변환하여 응답)


 5. 영화 등록하기
 - POST   /movie
 - 요청 BODY 데이터 (title, rating)
 - 응답 BODY 데이터 성공시 (statusCode = 200, msg = “ok”)
 - 응답 BODY 데이터 실패시 (statusCode = 500, msg = “fail”)

 6. 영화 삭제하기
 - DELETE   /movie/8
 - 응답 BODY 데이터 성공시 (statusCode = 200, msg = “ok”)
 - 응답 BODY 데이터 실패시 (statusCode = 500, msg = “fail”)
 

 7. 영화  수정하기
 - PUT    /movie/7
 - BODY 데이터 (title, rating)
 - 응답 BODY 데이터 성공시 (statusCode = 200, msg = “ok”)
 - 응답 BODY 데이터 실패시 (statusCode = 500, msg = “fail”)
 <br/>
 <hr/>

 ### 환경설정
 - windows10
 - sts 
 - jdk 1.8
 - Postman
 
 ![image](https://user-images.githubusercontent.com/44068819/105007341-344ef880-5a7b-11eb-8065-7e7532969299.png)
 
 ![image](https://user-images.githubusercontent.com/44068819/105007545-79732a80-5a7b-11eb-8616-383543059278.png)
 
 ### 모델
  ```java
 package com.cos.movie.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
	private int id;
	private double rating;
	private String title;
	private Timestamp makeDate;
}

 ```
 ### Repository
 ```java
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

 ```
 ### DTO
 
 #### CommonDto (HttpCode)
 ```java
 package com.cos.movie.domain;

import lombok.Data;

@Data
public class CommonDto<T> {
	private int statusCode;
	private T data;

	public CommonDto(int statusCode) {
		super();
		this.statusCode = statusCode;
	}
	
	public CommonDto(int statusCode,T data) {
		super();
		this.statusCode = statusCode;
		this.data = data;
	}
}

 ```
 #### JoinDto (영화추가)
 ```java
 package com.cos.movie.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class JoinReqDto {
	@NotNull(message = "fail")
	@NotBlank(message = "fail")
	private String title;

	@NotNull(message = "fail")
	private double rating;
}

 ```
 #### UpdateDto (영화 수정)
 ```java
 package com.cos.movie.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateReqDto {
	@NotNull(message = "fail")
	@NotBlank(message = "fail")
	private String title;
	@NotNull(message = "fail")
	private double rating;
}

 ```
 ### 컨트롤러
 
 ####  - 전체영화 가져오기
 ```java
 package com.cos.movie.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.movie.domain.CommonDto;
import com.cos.movie.domain.JoinReqDto;
import com.cos.movie.domain.Movie;
import com.cos.movie.domain.MovieRepository;
import com.cos.movie.domain.UpdateReqDto;

@RestController
public class MovieController {
	private MovieRepository movieRepository;
	
	public MovieController(MovieRepository movieRepository) {
		this.movieRepository=movieRepository;
	}
	
	//http://localhost:8080/movie
	@GetMapping("/movie")
	public CommonDto<List<Movie>> findAll(){
		System.out.println("모든 영화 찾기");
		return new CommonDto<>(HttpStatus.OK.value(), movieRepository.findAll());
		// MessageConverter (JavaObject -> Json String)
	}
 } 
 ```
  
 #### 결과
 
 ![image](https://user-images.githubusercontent.com/44068819/105008477-a247ef80-5a7c-11eb-9e33-2838a832de36.png)
 

 ##  - 영화 1건 가져오기(숫자로 구분 = 숫자는 영화의 id값)
  ```java
 //http://localhost:8080/movie/1
	@GetMapping("/movie/{id}")
	public CommonDto<Movie> findById(@PathVariable int id){
		System.out.println(id+"번 영화");
		return new CommonDto<>(HttpStatus.OK.value(),movieRepository.findById(id));
	}
 
 ```
 
 #### 결과
 
 ![image](https://user-images.githubusercontent.com/44068819/105008932-3914ac00-5a7d-11eb-8b33-693f8483c372.png)
 
 ##  - 영화 등록하기
  ```java
  @CrossOrigin
	@PostMapping("/movie")
	public CommonDto<String> save(@Valid @RequestBody JoinReqDto dto, BindingResult bindingResult){
		
		System.out.println("영화 등록하기");
		System.out.println("movie :"+dto);
		
		movieRepository.save(dto);
		if(dto.getTitle().equals("")||dto.getTitle().equals(null)) {
			return new CommonDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),"fail");
		}//영화 등록 시 제목 데이터값이 없으면 BODY데이터 실패로 간주
		return new CommonDto<>(HttpStatus.OK.value(),"ok");
	}
 
 ```
 
 ![image](https://user-images.githubusercontent.com/44068819/105009122-76793980-5a7d-11eb-9345-b66b2721ee40.png)
 
 ![image](https://user-images.githubusercontent.com/44068819/105009329-ade7e600-5a7d-11eb-8bcc-407098b49a9d.png)
 
 ##  - 영화 삭제하기
  ```java
 //http://localhost:8080/movie/1
	@DeleteMapping("/movie/{id}")
	public CommonDto delete(@PathVariable int id) {
		System.out.println("영화 삭제하기");
		movieRepository.delete(id);
		if(id>100 || id<0) {
			return new CommonDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),"fail");
		}//id값이 음수이거나 100이상이면 데이터 실패로 간주
		return new CommonDto<>(HttpStatus.OK.value(),"ok");
	}
 
 ```
 
 ![image](https://user-images.githubusercontent.com/44068819/105009235-97418f00-5a7d-11eb-85c6-655020a3227e.png)
 
 ![image](https://user-images.githubusercontent.com/44068819/105009448-d4a61c80-5a7d-11eb-969b-58043bf6497f.png)
 
 ##  - 영화 수정하기
  ```java
 @PutMapping("/movie/{id}")
	public CommonDto update(@PathVariable int id,@Valid @RequestBody UpdateReqDto dto, BindingResult bindingResult) {
		System.out.println("영화 수정하기");
		System.out.println("movie :"+dto);
		
		movieRepository.update(id, dto);

		if(dto.getTitle().equals("")||dto.getTitle().equals(null)) { //제목데이터값이 없으면 BODY데이터 실패로 간주
			return new CommonDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),"fail");		
		}
		return new CommonDto<>(HttpStatus.OK.value(),"ok");
	}
 ```
 
 ![image](https://user-images.githubusercontent.com/44068819/105009677-2189f300-5a7e-11eb-8dfa-002aafabf1c7.png)
 
 ![image](https://user-images.githubusercontent.com/44068819/105009734-31093c00-5a7e-11eb-9305-5a216f57797f.png)
 
 ## - BODY 데이터 전송 실패
 
 ![image](https://user-images.githubusercontent.com/44068819/105009908-6c0b6f80-5a7e-11eb-8325-d1edf373b8d9.png)
