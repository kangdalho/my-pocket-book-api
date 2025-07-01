# 📚My Pocket Book API

**My Pocket Book API는 사용자가 외부 Kakao API를 통해 책을 검색하고, 원하는 책을 등록하여 리뷰를 남길 수 있는 도서 서비스입니다.
JWT 기반 인증과 OAuth2(Google, Kakao) 로그인을 지원하며, Redis를 활용한 리뷰 인기순 캐싱 및 AWS 배포까지 고려된 실무형 백엔드 프로젝트입니다.**

## 📅개발 기간

**2025.5.27 ~ 2025.7.8**

## 📌개발 환경 및 기술 스택

<!-- Framework -->
<img src="https://img.shields.io/badge/Framework:-4B5563?style=for-the-badge"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.5.0-9CA3AF?style=for-the-badge">

<!-- Language -->
<h><img src="https://img.shields.io/badge/Language:-065F46?style=for-the-badge"> <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/17-9CA3AF?style=for-the-badge">

<!-- ORM -->
<img src="https://img.shields.io/badge/ORM:-6B21A8?style=for-the-badge"> <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"><img src="https://img.shields.io/badge/5.x-9CA3AF?style=for-the-badge"> <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">

<!-- Auto Audit -->
<h><img src="https://img.shields.io/badge/Auto_Audit:-9ACD32?style=for-the-badge"> <img src="https://img.shields.io/badge/JPA_Auditing-6DB33F?style=for-the-badge&logo=spring&logoColor=white">

<!-- Database -->
<h><img src="https://img.shields.io/badge/Database:-1E3A8A?style=for-the-badge"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/9.0.x-9CA3AF?style=for-the-badge">

<!-- Infra -->
<img src="https://img.shields.io/badge/Infra:-B91C1C?style=for-the-badge"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/3.0.504-9CA3AF?style=for-the-badge"> <img src="https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white">

<!-- Security -->
<h><img src="https://img.shields.io/badge/Security:-9ACD32?style=for-the-badge"> <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white"><img src="https://img.shields.io/badge/Google_OAuth2-4285F4?style=for-the-badge&logo=google&logoColor=white"><img src="https://img.shields.io/badge/Kakao_OAuth2-FFEB00?style=for-the-badge&logo=kakaotalk&logoColor=000000">

<!-- Code Reduction -->
<h><img src="https://img.shields.io/badge/Code_Reduction:-FF7F7F?style=for-the-badge"> <img src="https://img.shields.io/badge/Lombok-DC382D?style=for-the-badge&logo=lombok&logoColor=white">

<!-- Validation -->
<img src="https://img.shields.io/badge/Validation:-7BAAF7?style=for-the-badge"> <img src="https://img.shields.io/badge/Validation_Tool-0052CC?style=for-the-badge&logo=checkmarx&logoColor=white">

<!-- Build Tool -->
<h><img src="https://img.shields.io/badge/BUILDTool:-0369A1?style=for-the-badge"> <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white">

<!-- Test Tool -->
<h><img src="https://img.shields.io/badge/TestTool:-F59E0B?style=for-the-badge"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">

<!-- VCS -->
<h><img src="https://img.shields.io/badge/VCS:-1F2937?style=for-the-badge"> <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">

<!-- Communication -->
<h><img src="https://img.shields.io/badge/Communication:-3B0764?style=for-the-badge"> <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">

<!-- IDE -->
<img src="https://img.shields.io/badge/IDE:-7C3AED?style=for-the-badge"> <img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white"><img src="https://img.shields.io/badge/2024.1-9CA3AF?style=for-the-badge">

## 📃API 명세서

[My Pocket Book API](https://www.notion.so/2012dc3ef51480bfa92fe93a68037ff2?pvs=21)

## 📌주요 기능

| 도메인                 | 기능                | 상세 설명                                                                         |
|---------------------|-------------------|-------------------------------------------------------------------------------|
| 👤 **회원**           | **회원가입 / 로그인**    | - OAuth2 기반 로그인 (Google, Kakao)<br>- 최초 로그인 시 자동 회원가입<br>- JWT 토큰 발급 및 헤더에 포함 |
|                     | **내 프로필 조회**      | - 로그인한 사용자의 닉네임, 이메일 등 반환<br>- 마이페이지 구현 가능                                    |
| 🔍 **도서**           | **외부 API 검색**     | - Kakao Book API 연동<br>- 키워드 기반 도서 검색<br>- 검색 결과 응답 가공 후 사용자에게 전달             |
|                     | **도서 등록**         | - 검색한 도서를 서비스 DB에 등록<br>- 중복 등록 방지 (ISBN 기준)<br>- 등록된 도서만 리뷰, 찜 가능            |
| 🛒 **찜 (Wishlist)** | **도서 찜**          | - 로그인 사용자가 원하는 도서 찜 등록<br>- Redis 또는 DB에 사용자별 찜 정보 저장                         |
|                     | **찜 해제**          | - 이미 찜한 도서를 다시 클릭 시 찜 해제<br>- 사용자의 위시리스트에서 해당 도서 제거                           |
|                     | **찜 목록 조회**       | - 사용자가 찜한 도서 리스트 페이징 조회<br>- 정렬(최신순 등) 기능 지원                                  |
| 📝 **리뷰**           | **도서 리뷰 등록**      | - 도서 상세 페이지에서 리뷰 작성 가능<br>- 별점(1\~5점), 텍스트 작성<br>- 로그인 사용자만 작성 가능             |
|                     | **리뷰 수정/삭제**      | - 본인이 작성한 리뷰만 수정/삭제 가능                          |
|                     | **작성 리뷰 조회**      | - 사용자가 작성한 리뷰 리스트 페이징 조회<br>- 정렬(최신순 등) 기능 지원                                 |
|                     | **리뷰 인기순 정렬**     | - Redis에 리뷰 좋아요 수를 캐싱<br>- 좋아요 수 기준 리뷰 TOP 10 조회 API 제공<br>- 정렬/랭킹 기능 구현                                 |
| 📝**댓글**            | **댓글 등록/삭제**      | - 리뷰에 대한 댓글 작성 기능<br>- 댓글 수정/삭제는 작성자만 가능                                      |
|                     | **작성 댓글 조회**      | - 사용자가 리뷰에 작성한 댓글 리스트 페이징 조회<br>- 정렬(최신순 등) 기능 지원                             |
| ❤️ **리뷰 좋아요**       | **리뷰 좋아요 등록/ 취소** | - 로그인 한 사용자가 리뷰에 좋아요 등록<br>- 이미 좋아요한 리뷰를 다시 클릭 시 취소                           |
|                     | **좋아요 수 조회**      | - 특정 리뷰의 좋아요 수 반환                                                             |

## 📁프로젝트 구조

````
src/
├── config/              # 설정 (Security, Swagger, Redis 등)
├── controller/          # API 진입점
├── dto/                 # 요청/응답 DTO
├── entity/              # JPA 엔티티
├── exception/           # 커스텀 예외 및 전역 핸들링
├── repository/          # JPA Repository
├── security/            # 인증 관련 핵심 로직
├── service/             # 비즈니스 로직
├── resources/
│   ├── application.yml  # 환경 설정
│   ├── schema.sql       # 초기 테이블
````



## 📌팀 구성 및 담당 역할

| 이름        | 담당                              |
|-----------|---------------------------------|
| 🧑‍💻 강달호 | 회원 API, OAuth2 로그인(Google, Kakao) |
| 🧑‍💻 조유석 | 리뷰 API, 페이징 처리, Redis 캐시, AWS 배포|
| 🧑‍💻 정재민 | 콘텐츠 API, Kakao Book API 연동      |
| 🧑‍💻 황경민 | 댓글 API, 페이징 처리                  |
| 🧑‍💻 강다연 | 위시리스트 API, AWS 배포               |
| 🧑‍💻 이종현 | 좋아요 API, 페이징 처리                 |

## 🚀문의

#### 개발 관련 문의는 이메일로 부탁드립니다.

**👨‍💻Email: d4lh0w@naver.com**







