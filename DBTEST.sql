-- 1-1. 첫 번째 회원 삽입
INSERT INTO MEMBERS (name, email, passwd, point, rank) VALUES
('김철수', 'kim@example.com', 'password123', 15000, 'GOLD');

-- 1-2. 두 번째 회원 삽입
INSERT INTO MEMBERS (name, email, passwd, point, rank) VALUES
('이영희', 'lee@example.com', 'password456', 8000, 'SILVER');

-- 1-3. 세 번째 회원 삽입
INSERT INTO MEMBERS (name, email, passwd, point, rank) VALUES
('박민수', 'park@example.com', 'password789', 25000, 'PLATINUM');

-- 1-4. 네 번째 회원 삽입
INSERT INTO MEMBERS (name, email, passwd, point, rank) VALUES
('최지영', 'choi@example.com', 'password321', 5000, 'BRONZE');

-- 1-5. 다섯 번째 회원 삽입
INSERT INTO MEMBERS (name, email, passwd, point, rank) VALUES
('정우진', 'jung@example.com', 'password654', 12000, 'GOLD');

-- 2-1. 첫 번째 게시글 삽입
INSERT INTO POSTS (title, member_id, post_time, contents, view_count, reply_count) VALUES
('주식 투자 초보자 가이드', 1, '2025-06-15 10:30:00', '주식 투자를 시작하는 분들을 위한 기본 가이드입니다. 먼저 기업 분석부터 시작해보세요.', 150, 5);

-- 2-2. 두 번째 게시글 삽입
INSERT INTO POSTS (title, member_id, post_time, contents, view_count, reply_count) VALUES
('삼성전자 주가 전망', 2, '2025-06-16 14:20:00', '삼성전자의 최근 실적과 향후 전망에 대해 분석해보겠습니다.', 89, 3);

-- 2-3. 세 번째 게시글 삽입
INSERT INTO POSTS (title, member_id, post_time, contents, view_count, reply_count) VALUES
('코인 vs 주식 어떤 것이 좋을까?', 3, '2025-06-17 09:15:00', '암호화폐와 주식 투자의 장단점을 비교해보는 글입니다.', 234, 12);

-- 2-4. 네 번째 게시글 삽입
INSERT INTO POSTS (title, member_id, post_time, contents, view_count, reply_count) VALUES
('배당주 추천 리스트', 4, '2025-06-17 16:45:00', '안정적인 배당수익을 원하는 분들을 위한 추천 종목들입니다.', 67, 2);

-- 2-5. 다섯 번째 게시글 삽입
INSERT INTO POSTS (title, member_id, post_time, contents, view_count, reply_count) VALUES
('투자 포트폴리오 공유', 5, '2025-06-18 08:30:00', '제가 운용하고 있는 포트폴리오를 공유합니다.', 45, 1);
-- 3-1. 첫 번째 주식 데이터 삽입 (김철수 - 삼성전자)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('삼성전자', 10, 75000, 1);

-- 3-2. 두 번째 주식 데이터 삽입 (김철수 - SK하이닉스)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('SK하이닉스', 5, 120000, 1);

-- 3-3. 세 번째 주식 데이터 삽입 (이영희 - NAVER)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('NAVER', 8, 180000, 2);

-- 3-4. 네 번째 주식 데이터 삽입 (이영희 - 카카오)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('카카오', 15, 45000, 2);

-- 3-5. 다섯 번째 주식 데이터 삽입 (박민수 - LG화학)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('LG화학', 3, 350000, 3);

-- 3-6. 여섯 번째 주식 데이터 삽입 (박민수 - 현대차)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('현대차', 7, 190000, 3);

-- 3-7. 일곱 번째 주식 데이터 삽입 (최지영 - KB금융)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('KB금융', 20, 55000, 4);

-- 3-8. 여덟 번째 주식 데이터 삽입 (최지영 - 신한지주)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('신한지주', 12, 42000, 4);

-- 3-9. 아홉 번째 주식 데이터 삽입 (정우진 - 포스코홀딩스)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('포스코홀딩스', 4, 280000, 5);

-- 3-10. 열 번째 주식 데이터 삽입 (정우진 - 셀트리온)
INSERT INTO STOCKS (stock_name, count, price, member_id) VALUES
('셀트리온', 6, 160000, 5);

-- 4-1. 첫 번째 댓글 삽입 (게시글 1번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(1, 2, 0, 0, '정말 유용한 정보네요! 감사합니다.', '2025-06-15 11:00:00');

-- 4-2. 두 번째 댓글 삽입 (게시글 1번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(1, 3, 0, 0, '초보자에게 딱 맞는 설명이에요.', '2025-06-15 12:30:00');

-- 4-3. 세 번째 댓글 삽입 (게시글 1번에 대한 대댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(1, 4, 1, 1, '저도 동감합니다. 특히 기업분석 부분이 도움됐어요.', '2025-06-15 13:15:00');

-- 4-4. 네 번째 댓글 삽입 (게시글 1번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(1, 5, 0, 0, '다음 글도 기대하겠습니다!', '2025-06-15 14:20:00');

-- 4-5. 다섯 번째 댓글 삽입 (게시글 1번에 대한 대댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(1, 1, 4, 1, '감사합니다. 더 자세한 내용은 다음 글에서 다루겠습니다.', '2025-06-15 15:00:00');

-- 4-6. 여섯 번째 댓글 삽입 (게시글 2번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(2, 1, 0, 0, '삼성전자 전망이 밝아 보이네요.', '2025-06-16 15:00:00');

-- 4-7. 일곱 번째 댓글 삽입 (게시글 2번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(2, 3, 0, 0, '실적 분석이 정확한 것 같습니다.', '2025-06-16 16:30:00');

-- 4-8. 여덟 번째 댓글 삽입 (게시글 2번에 대한 대댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(2, 4, 1, 1, '저도 매수를 고려해봐야겠어요.', '2025-06-16 17:00:00');

-- 4-9. 아홉 번째 댓글 삽입 (게시글 3번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(3, 1, 0, 0, '둘 다 장단점이 있는 것 같아요.', '2025-06-17 10:00:00');

-- 4-10. 열 번째 댓글 삽입 (게시글 3번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(3, 2, 0, 0, '분산투자가 답인 것 같습니다.', '2025-06-17 11:30:00');

-- 4-11. 열한 번째 댓글 삽입 (게시글 3번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(3, 4, 0, 0, '코인은 변동성이 너무 커서 조심스럽네요.', '2025-06-17 12:00:00');

-- 4-12. 열두 번째 댓글 삽입 (게시글 3번에 대한 대댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(3, 5, 2, 1, '맞습니다. 적절한 비율로 나누는 게 중요해요.', '2025-06-17 13:15:00');

-- 4-13. 열세 번째 댓글 삽입 (게시글 4번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(4, 1, 0, 0, '배당주 정보 감사합니다!', '2025-06-17 17:30:00');

-- 4-14. 열네 번째 댓글 삽입 (게시글 4번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(4, 3, 0, 0, '안정적인 수익을 원해서 관심 있었는데 도움됐어요.', '2025-06-17 18:00:00');

-- 4-15. 열다섯 번째 댓글 삽입 (게시글 5번에 대한 댓글)
INSERT INTO REPLY (post_id, member_id, parent_id, layer, contents, reply_time) VALUES
(5, 2, 0, 0, '포트폴리오 구성이 인상적이네요!', '2025-06-18 09:00:00');

COMMIT;

SELECT * FROM MEMBERS;
SELECT * FROM POSTS;
SELECT * FROM STOCKS;
SELECT * FROM REPLY;
