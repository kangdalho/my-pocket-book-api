#!/usr/bin/env python3
"""
검색 API 전용 성능 테스트 - Full-Text Search 실험용
토큰을 미리 설정하여 순수하게 검색 성능만 측정
"""

from locust import HttpUser, task, between, events
import random
import json
import time

class SearchOnlyUser(HttpUser):
    """검색 API만 테스트하는 사용자"""

    # 요청 간 대기 시간
    wait_time = between(0.5, 2)  # 더 빠른 테스트를 위해 단축

    def on_start(self):
        """테스트 시작 시 토큰 설정"""
        # 🔑 여기에 미리 발급받은 JWT 토큰을 입력하세요
        self.jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwibWVtYmVySWQiOjEsImV4cCI6MTc1MTI4OTg2NCwiaWF0IjoxNzUxMjg2MjY0fQ.shAHyw7SVXg3IZg1wg01kBpinXWlmRCdRSow2OFxRcw"

        # 검색할 키워드 리스트
        self.search_keywords = [
            # 프로그래밍 관련
            "자바", "파이썬", "자바스크립트", "스프링", "리액트", "노드", "프로그래밍", "개발", "코딩",
            # 데이터사이언스 관련
            "데이터", "분석", "머신러닝", "딥러닝", "AI", "빅데이터", "통계", "알고리즘",
            # 웹개발 관련
            "웹", "HTML", "CSS", "서버", "데이터베이스", "API", "백엔드", "프론트엔드",
            # 모바일 관련
            "안드로이드", "iOS", "모바일", "앱", "스위프트", "코틀린", "플러터",
            # 인프라 관련
            "클라우드", "AWS", "도커", "쿠버네티스", "데브옵스", "네트워크", "보안"
        ]

        # 검색 성공 통계 (옵션)
        self.search_count = 0

    def get_headers(self):
        """인증 헤더 반환"""
        return {
            "Authorization": f"Bearer {self.jwt_token}",
            "Content-Type": "application/json"
        }

    @task(10)  # 가중치 10 - 단일 키워드 검색 (가장 일반적)
    def search_single_keyword(self):
        """단일 키워드 검색"""
        keyword = random.choice(self.search_keywords)

        params = {
            "keyword": keyword,
            "page": random.randint(0, 2),  # 0-2 페이지
            "size": random.choice([5, 10, 20]),
            "sort": random.choice([
                "title,ASC", "title,DESC"
                # "createdAt,ASC", "createdAt,DESC"
            ])
        }

        self._perform_search(keyword, params)

    @task(5)  # 가중치 5 - 복합 키워드 검색
    def search_multiple_keywords(self):
        """복합 키워드 검색"""
        keyword_combinations = [
            "자바 스프링", "파이썬 데이터", "웹 개발", "모바일 앱",
            "클라우드 서버", "데이터 분석", "머신러닝 AI", "프론트엔드 리액트"
        ]

        keyword = random.choice(keyword_combinations)

        params = {
            "keyword": keyword,
            "page": 0,
            "size": 10,
            # "sort": "createdAt,DESC"
        }

        self._perform_search(keyword, params)

    @task(3)  # 가중치 3 - 긴 검색어
    def search_long_phrase(self):
        """긴 문장 검색"""
        long_phrases = [
            "실무 중심의 프로그래밍 가이드",
            "초보자를 위한 개발 입문서",
            "실전 프로젝트 경험을 제공하는",
            "최신 기술 동향과 실무 노하우",
            "기초부터 고급까지 단계별 학습"
        ]

        keyword = random.choice(long_phrases)

        params = {
            "keyword": keyword,
            "page": 0,
            "size": 15,
            "sort": "title,ASC"
        }

        self._perform_search(keyword, params)

    @task(2)  # 가중치 2 - 빈 결과 검색
    def search_no_results(self):
        """결과가 없는 검색"""
        no_result_keywords = [
            "존재하지않는키워드", "NONEXISTENT", "빈결과테스트"
        ]

        keyword = random.choice(no_result_keywords)

        params = {
            "keyword": keyword,
            "page": 0,
            "size": 10,
            # "sort": "createdAt,DESC"
        }

        self._perform_search(keyword, params)

    @task(1)  # 가중치 1 - 깊은 페이징
    def search_deep_pagination(self):
        """깊은 페이징 성능 테스트"""
        keyword = random.choice(["자바", "파이썬", "웹"])  # 결과가 많은 키워드

        params = {
            "keyword": keyword,
            "page": random.randint(5, 15),  # 깊은 페이지
            "size": 20,
            "sort": "title,DESC"
        }

        self._perform_search(keyword, params)

    def _perform_search(self, keyword: str, params: dict):
        """실제 검색 API 호출"""
        headers = self.get_headers()

        with self.client.get("/api/contents/books/search",
                             params=params,
                             headers=headers,
                             catch_response=True) as response:

            self.search_count += 1

            if response.status_code == 200:
                try:
                    data = response.json()
                    if "data" in data and "content" in data["data"]:
                        content = data["data"]["content"]
                        response.success()

                        # 간헐적으로 결과 출력 (성능에 영향 최소화)
                        if self.search_count % 100 == 0:
                            print(f"검색 완료: '{keyword}' -> {len(content)}개 결과")
                    else:
                        response.failure("응답 구조 오류")
                except:
                    response.failure("JSON 파싱 오류")
            else:
                response.failure(f"HTTP {response.status_code}")

# 전역 통계 출력
@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """테스트 종료 시 실행"""
    print("\n검색 전용 성능 테스트 완료!")
    print("결과를 CSV 파일에서 확인하세요.")

    # 사용법:
    # 1. 토큰 발급 후 jwt_token 변수에 입력
    # 2. LIKE 쿼리 성능: locust -f search_only_locustfile.py --headless -u 100 -r 10 -t 600s --csv=like_baseline
    # 3. Full-Text 성능: locust -f search_only_locustfile.py --headless -u 100 -r 10 -t 600s --csv=fulltext_result

    @task(10)  # 가중치 10 - 단일 키워드 검색 (가장 일반적)
    def search_single_keyword(self):
        """단일 키워드 검색"""
        keyword = random.choice(self.search_keywords)

        params = {
            "keyword": keyword,
            "page": random.randint(0, 2),  # 0-2 페이지
            "size": random.choice([5, 10, 20]),
            "sort": random.choice([
                "title,ASC", "title,DESC",
                # "createdAt,ASC", "createdAt,DESC"
            ])
        }

        self._perform_search("단일키워드", keyword, params)

    @task(5)  # 가중치 5 - 복합 키워드 검색
    def search_multiple_keywords(self):
        """복합 키워드 검색"""
        # 관련된 키워드 2개 조합
        keyword_combinations = [
            "자바 스프링", "파이썬 데이터", "웹 개발", "모바일 앱",
            "클라우드 서버", "데이터 분석", "머신러닝 AI", "프론트엔드 리액트"
        ]

        keyword = random.choice(keyword_combinations)

        params = {
            "keyword": keyword,
            "page": 0,
            "size": 10,
            # "sort": "createdAt,DESC"
        }

        self._perform_search("복합키워드", keyword, params)

    @task(3)  # 가중치 3 - 긴 검색어
    def search_long_phrase(self):
        """긴 문장 검색"""
        long_phrases = [
            "실무 중심의 프로그래밍 가이드",
            "초보자를 위한 개발 입문서",
            "실전 프로젝트 경험을 제공하는",
            "최신 기술 동향과 실무 노하우",
            "기초부터 고급까지 단계별 학습"
        ]

        keyword = random.choice(long_phrases)

        params = {
            "keyword": keyword,
            "page": 0,
            "size": 15,
            "sort": "title,ASC"
        }

        self._perform_search("긴문장", keyword, params)

    @task(2)  # 가중치 2 - 빈 결과 검색 (존재하지 않는 키워드)
    def search_no_results(self):
        """결과가 없는 검색 (성능 측정용)"""
        no_result_keywords = [
            "존재하지않는키워드", "NONEXISTENT", "빈결과테스트",
            "ㅁㄴㅇㄹ", "12345678", "!@#$%^&*"
        ]

        keyword = random.choice(no_result_keywords)

        params = {
            "keyword": keyword,
            "page": 0,
            "size": 10,
            # "sort": "createdAt,DESC"
        }

        self._perform_search("빈결과", keyword, params)

    @task(1)  # 가중치 1 - 페이징 성능 테스트
    def search_deep_pagination(self):
        """깊은 페이징 성능 테스트"""
        keyword = random.choice(["자바", "파이썬", "웹"])  # 결과가 많은 키워드

        params = {
            "keyword": keyword,
            "page": random.randint(5, 15),  # 깊은 페이지
            "size": 20,
            "sort": "title,DESC"
        }

        self._perform_search("깊은페이징", keyword, params)

    def _perform_search(self, search_type: str, keyword: str, params: dict):
        """실제 검색 API 호출"""
        headers = self.get_headers()

        # 검색 시작 시간 기록
        start_time = time.time()

        with self.client.get("/api/contents/books/search",
                             params=params,
                             headers=headers,
                             catch_response=True) as response:

            # 응답 시간 계산
            response_time = (time.time() - start_time) * 1000  # ms

            self.search_stats["total_searches"] += 1

            if response.status_code == 200:
                try:
                    data = response.json()

                    if "data" in data and "content" in data["data"]:
                        content = data["data"]["content"]
                        total_elements = data["data"].get("totalElements", 0)

                        if len(content) > 0:
                            self.search_stats["successful_searches"] += 1
                            print(f"{search_type} 검색 '{keyword}': {len(content)}개 결과, {total_elements}개 전체, {response_time:.2f}ms")
                        else:
                            self.search_stats["empty_results"] += 1
                            print(f"{search_type} 검색 '{keyword}': 결과 없음, {response_time:.2f}ms")

                        response.success()
                    else:
                        print(f"{search_type} 검색 응답 구조 오류")
                        response.failure("응답 구조 오류")
                        self.search_stats["failed_searches"] += 1

                except json.JSONDecodeError:
                    print(f"{search_type} 검색 JSON 파싱 오류")
                    response.failure("JSON 파싱 오류")
                    self.search_stats["failed_searches"] += 1

            elif response.status_code == 401:
                print("토큰 만료 - 재로그인 시도")
                self.login()
                response.failure("토큰 만료")
                self.search_stats["failed_searches"] += 1

            else:
                print(f"{search_type} 검색 실패: {response.status_code}")
                response.failure(f"HTTP {response.status_code}")
                self.search_stats["failed_searches"] += 1

    def on_stop(self):
        """테스트 종료 시 통계 출력"""
        if self.search_stats["total_searches"] > 0:
            success_rate = (self.search_stats["successful_searches"] / self.search_stats["total_searches"]) * 100
            empty_rate = (self.search_stats["empty_results"] / self.search_stats["total_searches"]) * 100

            print(f"\n개인 검색 통계:")
            print(f"  • 총 검색: {self.search_stats['total_searches']}회")
            print(f"  • 성공: {self.search_stats['successful_searches']}회 ({success_rate:.1f}%)")
            print(f"  • 빈 결과: {self.search_stats['empty_results']}회 ({empty_rate:.1f}%)")
            print(f"  • 실패: {self.search_stats['failed_searches']}회")

# 전역 통계 수집
@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """전체 테스트 종료 시 실행"""
    print("\n검색 전용 성능 테스트 완료!")
    print("다음 단계: CSV 결과를 분석하여 LIKE 쿼리 성능 베이스라인 확인")

# 사용법:
# 1. 데이터 생성: python book_data_generator.py
# 2. LIKE 쿼리 성능 측정: locust -f search_only_locustfile.py --headless -u 50 -r 5 -t 600s --csv=like_query_baseline
# 3. Full-Text Search 구현 후: locust -f search_only_locustfile.py --headless -u 50 -r 5 -t 600s --csv=fulltext_search_result