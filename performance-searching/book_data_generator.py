#!/usr/bin/env python3
"""
도서 테스트 데이터 생성기
Full-Text Search 성능 테스트를 위한 다양한 키워드 포함 도서 데이터 대량 생성
"""

import requests
import json
import random
import time
from typing import List, Dict

class BookDataGenerator:
    def __init__(self, api_base_url: str):
        self.api_base_url = api_base_url
        self.jwt_token = None

        # 다양한 분야별 키워드 데이터셋
        self.categories = {
            "프로그래밍": {
                "keywords": ["자바", "파이썬", "자바스크립트", "스프링", "리액트", "노드", "프로그래밍", "개발", "코딩"],
                "titles": ["실전 자바 프로그래밍", "파이썬 마스터하기", "자바스크립트 완벽 가이드", "스프링 부트 실전", "리액트 개발자 되기"],
                "publishers": ["한빛미디어", "위키북스", "길벗", "이지스퍼블리싱", "인사이트"]
            },
            "데이터사이언스": {
                "keywords": ["데이터", "분석", "머신러닝", "딥러닝", "AI", "빅데이터", "통계", "알고리즘"],
                "titles": ["데이터 분석 실무", "머신러닝 첫걸음", "딥러닝의 모든 것", "파이썬 데이터 분석", "빅데이터 처리"],
                "publishers": ["에이콘", "제이펍", "한빛미디어", "위키북스"]
            },
            "웹개발": {
                "keywords": ["웹", "HTML", "CSS", "서버", "데이터베이스", "API", "백엔드", "프론트엔드"],
                "titles": ["웹 개발의 기초", "HTML5 완전 정복", "CSS 마스터", "서버 개발 실무", "데이터베이스 설계"],
                "publishers": ["길벗", "한빛미디어", "인사이트"]
            },
            "모바일": {
                "keywords": ["안드로이드", "iOS", "모바일", "앱", "개발", "스위프트", "코틀린", "플러터"],
                "titles": ["안드로이드 앱 개발", "iOS 프로그래밍", "모바일 앱 제작", "스위프트 프로그래밍", "코틀린 실전"],
                "publishers": ["제이펍", "에이콘", "위키북스"]
            },
            "인프라": {
                "keywords": ["클라우드", "AWS", "도커", "쿠버네티스", "데브옵스", "서버", "네트워크", "보안"],
                "titles": ["클라우드 아키텍처", "AWS 완벽 가이드", "도커 컨테이너", "쿠버네티스 실전", "데브옵스 방법론"],
                "publishers": ["한빛미디어", "에이콘", "길벗"]
            }
        }

    def login(self, nickname: str, password: str) -> bool:
        """로그인하여 JWT 토큰 획득"""
        login_url = f"{self.api_base_url}/api/members/login"
        login_data = {
            "nickname": nickname,
            "password": password
        }

        try:
            response = requests.post(login_url, json=login_data)
            if response.status_code == 200:
                # 헤더에서 Authorization 토큰 추출
                auth_header = response.headers.get("Authorization")
                if auth_header and auth_header.startswith("Bearer "):
                    self.jwt_token = auth_header[7:]  # "Bearer " 제거
                    print(f"로그인 성공 - 토큰: {self.jwt_token[:20]}...")
                    return True
                else:
                    print(f"토큰을 찾을 수 없음. 응답 헤더: {dict(response.headers)}")
                    return False
            else:
                print(f"로그인 실패: {response.status_code}")
                return False
        except Exception as e:
            print(f"로그인 오류: {e}")
            return False

    def generate_book_data(self, category_name: str, index: int) -> Dict:
        """특정 카테고리의 도서 데이터 생성"""
        category = self.categories[category_name]

        # 해당 카테고리의 키워드 2-3개 선택
        selected_keywords = random.sample(category["keywords"], random.randint(2, 3))
        main_keyword = selected_keywords[0]

        # 제목 생성 (키워드 포함)
        base_title = random.choice(category["titles"])
        title = f"{base_title} {index + 1}판"

        # 요약 생성 (선택된 키워드들 포함)
        summary_templates = [
            f"이 책은 {main_keyword}에 대한 실무 중심의 내용을 다룹니다. {', '.join(selected_keywords)} 기술을 활용한 실제 프로젝트 경험을 제공하며, 초보자부터 중급자까지 체계적으로 학습할 수 있습니다.",
            f"{main_keyword} 개발자를 위한 완벽한 가이드북입니다. {', '.join(selected_keywords)} 관련 최신 기술 동향과 실전 노하우를 담았으며, 현업에서 바로 활용 가능한 예제를 제공합니다.",
            f"{', '.join(selected_keywords)}를 처음 접하는 개발자들을 위해 기초부터 고급까지 단계별로 설명합니다. {main_keyword} 전문가가 되기 위한 필수 지식과 실무 팁을 모두 담았습니다.",
            f"최신 {main_keyword} 기술 스택을 활용한 프로젝트 개발 방법을 소개합니다. {', '.join(selected_keywords)} 생태계의 이해부터 실제 서비스 구축까지 전 과정을 다룹니다."
        ]

        return {
            "isbn": f"978{random.randint(1000000000, 9999999999)}",
            "title": title,
            "thumbnail": f"https://example.com/thumbnails/{category_name.lower()}_{index}.jpg",
            "bookLink": f"https://example.com/books/{category_name.lower()}_{index}",
            "summary": random.choice(summary_templates),
            "salePrice": str(random.randint(15000, 45000)),
            "status": random.choice(["입고", "품절", "예약"])
            # "createdAt": "2024-01-01"
        }

    def create_book(self, book_data: Dict) -> bool:
        """도서 등록 API 호출"""
        if not self.jwt_token:
            print("JWT 토큰이 없습니다. 먼저 로그인하세요.")
            return False
        # print(self.jwt_token)
        create_url = f"{self.api_base_url}/api/contents"
        headers = {
            "Authorization": f"Bearer {self.jwt_token}",
            "Content-Type": "application/json"
        }

        try:
            response = requests.post(create_url, json=book_data, headers=headers)
            if response.status_code in [200, 201]:
                return True
            else:
                print(f"도서 등록 실패: {response.status_code} - {book_data['title']}")
                return False
        except Exception as e:
            print(f"도서 등록 오류: {e}")
            return False

    def generate_test_dataset(self, books_per_category: int = 100) -> Dict[str, int]:
        """카테고리별로 테스트 데이터 생성"""
        total_created = 0
        results = {}

        print(f"테스트 데이터 생성 시작 (카테고리당 {books_per_category}개)")
        print(f"총 예상 도서 수: {len(self.categories)} × {books_per_category} = {len(self.categories) * books_per_category}개\n")

        for category_name in self.categories.keys():
            print(f"{category_name} 카테고리 도서 생성 중...")
            category_success = 0

            for i in range(books_per_category):
                book_data = self.generate_book_data(category_name, i)

                if self.create_book(book_data):
                    category_success += 1
                    total_created += 1

                    # 진행률 출력
                    if (i + 1) % 20 == 0:
                        print(f"  {category_name}: {i + 1}/{books_per_category} 완료")

                # API 부하 방지를 위한 지연
                time.sleep(0.1)

            results[category_name] = category_success
            print(f"{category_name} 완료: {category_success}/{books_per_category}개 성공\n")

        print(f"테스트 데이터 생성 완료!")
        print(f"총 생성된 도서: {total_created}개")
        print("\n카테고리별 결과:")
        for category, count in results.items():
            print(f"  • {category}: {count}개")

        return results

    def verify_search_data(self) -> Dict[str, int]:
        """검색 테스트를 위한 데이터 검증"""
        print("\n 검색 데이터 검증 중...")

        search_url = f"{self.api_base_url}/api/contents/books/search"
        headers = {
            "Authorization": f"Bearer {self.jwt_token}",
            "Content-Type": "application/json"
        }

        # 각 카테고리의 주요 키워드로 검색 테스트
        test_keywords = ["자바", "파이썬", "데이터", "웹", "클라우드"]
        results = {}

        for keyword in test_keywords:
            try:
                params = {"keyword": keyword, "page": 0, "size": 10}
                response = requests.get(search_url, params=params, headers=headers)

                if response.status_code == 200:
                    data = response.json()
                    if "data" in data and "content" in data["data"]:
                        count = len(data["data"]["content"])
                        total = data["data"].get("totalElements", 0)
                        results[keyword] = total
                        print(f"  '{keyword}' 검색: {total}개 결과")
                    else:
                        results[keyword] = 0
                        print(f"  '{keyword}' 검색: 결과 없음")
                else:
                    print(f"  '{keyword}' 검색 실패: {response.status_code}")
                    results[keyword] = -1
            except Exception as e:
                print(f"  '{keyword}' 검색 오류: {e}")
                results[keyword] = -1

        return results

def main():
    """메인 실행 함수"""
    # 설정
    API_BASE_URL = "http://localhost:8080"
    NICKNAME = "test"  # 실제 테스트 계정으로 변경
    PASSWORD = "testpassword"  # 실제 비밀번호로 변경
    BOOKS_PER_CATEGORY = 50  # 카테고리당 생성할 도서 수

    print("Full-Text Search 성능 테스트용 데이터 생성기")
    print("=" * 60)

    # 데이터 생성기 초기화
    generator = BookDataGenerator(API_BASE_URL)

    # 로그인
    print("로그인 중...")
    if not generator.login(NICKNAME, PASSWORD):
        print("로그인 실패. 계정 정보를 확인하세요.")
        return

    # 테스트 데이터 생성
    results = generator.generate_test_dataset(BOOKS_PER_CATEGORY)

    # 검색 데이터 검증
    search_results = generator.verify_search_data()

    # 최종 결과 요약
    print("\n" + "=" * 60)
    print("데이터 생성 완료! 이제 성능 테스트를 시작할 수 있습니다.")
    print("\n다음 단계:")
    print("1. Locust 검색 전용 테스트 파일 실행")
    print("2. LIKE 쿼리 성능 측정 (Before)")
    print("3. Full-Text Search 구현")
    print("4. Full-Text Search 성능 측정 (After)")
    print("5. 결과 비교 분석")

if __name__ == "__main__":
    main()