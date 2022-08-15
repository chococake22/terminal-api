# terminalApi

## 장호원버스터미널 API 


### <목적>

 - 경기도 이천시 장호원읍은 읍 단위의 작은 시골 동네이다.
 - 이곳의 교통 인프라는 매우 낙후되어 있으며 특히 최근 코로나로 인해서 배차 시간의 변동이 잦아 많은 사람들이 불편함을 겪고 있다.
 - 다음 카페를 통해 터미널 시간표를 확인할 수 있으나 다음 계정을 통해 로그인을 해야 확인이 가능하다.
 - 그렇기 때문에 프로젝트를 통해 터미널 시간표와 공지사항 등을 알 수 있는 웹서비스를 기획하고자 한다.


### <요구사항>

#### 회원관리

 - 회원 계정을 생성할 수 있다.
 - 회원 가입시 아이디, 비밀번호, 이름, 이메일, 연락처를 필수로 입력한다.
 - 회원은 아이디와 이름을 제외한 자신의 정보를 수정할 수 있다.


#### 게시판

 - 게시판에서는 공지사항과 자유로운 글을 작성, 수정, 삭제할 수 있다.
 - 로그인을 한 사용자만 글쓰기, 수정, 삭제를 할 수 있다.
 - 로그인을 하지 않아도 글의 내용과 댓글을 확인하고 첨부파일을 다운받을 수 있다.
 - 댓글 작성, 수정, 삭제는 회원만 가능하다.
 - 본인의 글만 본인이 수정, 삭제가 가능하다.
 - 글쓰기는 제목과 내용을 반드시 입력해야 한다.
 - 파일 첨부가 가능하고 한 게시글에 여러 파일을 첨부할 수 있다.
 - 게시판 검색은 전체, 제목별, 작성자별, 내용별로 나누어서 할 수 있다.


#### 버스 시간표

 - 관리자 계정만 새로운 버스 시간표를 추가할 수 있다.
 - 버스 시간표는 출발지, 도착지, 출발시간, 버스회사, 경유지, 특이사항을 입력할 수 있다.
 - 경유지와 특이사항을 제외한 항목은 필수로 입력한다.
 - 회원은 원하는 시간표만 골라서 내 버스 시간표로 구성할 수 있다.
 - 회원이 이미 구성한 시간표 항목은 중복으로 구성할 수 없다.


### <기술스택>

 - Java 11
 - Spring Boot 2.7
 - MySQL
 - Spring Security 5.7
 - JPA(hibernate)
 - Swagger 3.0
 - Intellij
 - gradle


### <DB 설계>


![TerminalApi](https://user-images.githubusercontent.com/93370148/184646626-f4638103-e7fc-4bc8-b131-72d1d70c9905.png)



### <API 설계>


#### 회원 관리

 - 회원가입 : POST - /api/v1/user
 - 회원 목록 조회 : GET - /api/v1/user/list
 - 회원 개별 조회 : GET - /api/v1/user/{userNo} 
 - 회원정보 수정 : PUT - /api/v1/user
 - 토큰 발급 테스트 : GET - /api/v1/token
 - 액세스 토큰 재발급 : GET - /api/v1/access-token



#### 게시판 관리

 - 개별 게시글 상세 조회 : GET - /api/v1/board/{boardNo}
 - 게시글 목록 조회 : GET - /api/v1/board/list
 - 게시글 생성 : POST - /api/v1/board
 - 게시글 수정 : PUT - /api/v1/board/{boardNo}
 - 게시글 삭제 : DELETE - /api/v1/board/{boardNo}
 - 게시글 검색 : GET - /api/v1/board/search



#### 댓글 관리

 - 댓글 작성 : POST - /api/v1/{boardNo}/comment
 - 댓글 목록 조회 : GET - /api/v1/{boardNo}/comment/list
 - 댓글 수정 : PUT - /api/v1/comment/{commentNo}
 - 댓글 삭제 : DELETE - /api/v1/comment/{commentNo}



#### 버스 시간표 관리

 - 내 시간표 조회 : GET - /api/v1/bustime 
 - 내 시간표 등록 : POST - /api/v1/bustime/{busTimeNo}
 - 내 시간표 삭제 : DELETE - /api/v1/bustime/{myTimeNo}
 - 신규 버스 시간표 등록 : POST - /api/v1/bustime/admin
 - 버스 시간표 삭제 : DELETE - /api/v1/bustime/admin/{busTimeNo}



#### 파일 관리

 - 파일 저장 : POST - /api/v1/board/file/{boardNo}
 - 파일 다운로드 : GET - /api/v1/board/file/{fileName}
 - 파일 삭제 : DELETE - /api/v1/board/file/{boardNo}/{fileName}



### <느낀점>

 - REST API 형식에 맞는 API 개발에 대해서 알 수 있었다.
 - DB 설계를 어떻게 하느냐에 따라서 다양한 형태의 구조가 나올 수 있음을 확인할 수 있었다.
 - 간단한 기능이어도 실제 구현 단계에서 다양한 경우의 수를 생각해야함을 느낄 수 있었다.



