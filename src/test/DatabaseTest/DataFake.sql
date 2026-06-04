-- Fake data user
INSERT INTO users (email, role)
SELECT
    substr(md5(random()::text), 1, 8) || '@gmail.com',
    'USER'
FROM generate_series(1, 1000);

-- FakeData Comment
INSERT INTO movie_comments (content, rating, movie_id, user_id, created_at)
SELECT
    -- 1. Ghép chuỗi văn bản ngẫu nhiên từ 3 mảng (Có tới 3,600 tổ hợp khác nhau)
    (ARRAY['Bộ phim', 'Tác phẩm này', 'Bộ phim điện ảnh này', 'Phim', 'Siêu phẩm này', 'Bom tấn này', 'Trải nghiệm điện ảnh này', 'Nội dung phim', 'Dàn diễn viên trong phim', 'Kịch bản phim', 'Tổng thể bộ phim', 'Phim mới ra mắt này', 'Dự án điện ảnh này', 'Cốt truyện của phim', 'Suất chiếu này'])[floor(r1 * 15 + 1)::int]
    || ' ' ||
    (ARRAY['rất hấp dẫn và lôi cuốn từ đầu đến cuối', 'khá thú vị, mang tính giải trí cao', 'được đầu tư cực kỳ kỹ lưỡng về mặt hình ảnh', 'có nội dung sáng tạo, phá cách và đầy bất ngờ', 'mang lại nhiều cung bậc cảm xúc cho người xem', 'xem mà cuốn không thể rời mắt khỏi màn hình', 'kịch bản có chiều sâu với nhiều thông điệp nhân văn', 'diễn xuất của dàn cast phải nói là đỉnh thực sự', 'kỹ xảo và phần âm nhạc siêu hoành tráng, đã tai', 'nhịp phim hơi chậm ở hồi đầu nhưng càng về sau càng cuốn', 'có vài pha plot twist bẻ lái khét lẹt không đỡ nổi', 'làm rất tốt trong việc dẫn dắt tâm lý khán giả', 'xây dựng bối cảnh và tạo hình nhân vật quá xuất sắc', 'không quá xuất sắc nhưng vừa vặn, dễ xem', 'được triển khai mạch lạc, không bị dài dòng lê thê', 'đã chạm đến cảm xúc và lấy đi nước mắt của tôi'])[floor(r2 * 16 + 1)::int]
    || '. ' ||
    (ARRAY['Tôi sẽ giới thiệu cho bạn bè và người thân cùng ra rạp.', 'Chắc chắn sẽ mua vé xem lại lần hai để cảm nhận kỹ hơn.', 'Rất đáng để trải nghiệm cùng người yêu dịp cuối tuần.', 'Có vài điểm trừ nhỏ ở đoạn kết nhưng tổng thể vẫn rất ổn.', 'Nhìn chung khá hài lòng với số tiền vé và thời gian bỏ ra.', 'Xứng đáng nhận điểm 10/10 vì sự chỉn chu của ekip!', 'Xem xong mà vẫn thấy bồi hồi, ám ảnh mãi không thôi.', 'Ai chưa xem thì nên đặt vé đi xem ngay đi, không phí đâu.', 'Không uổng công mình đã kỳ vọng và chờ đợi suốt thời gian qua.', 'Một bộ phim trọn vẹn, rất lâu rồi mới xem được một phim hay thế này.', 'Mọi người nên đi xem để ủng hộ nền điện ảnh nước nhà nhé.', 'Điểm trừ duy nhất là rạp hôm nay hơi ồn, còn phim thì hoàn hảo.', 'Cực kỳ đề xuất cho những ai đam mê thể loại này.', 'Xem giải trí ổn áp, không có gì để chê trách nhiều.', 'Chắc chắn sẽ nằm trong top những phim hay nhất năm nay của tôi.'])[floor(r3 * 15 + 1)::int], -- ĐÃ THÊM DẤU PHẨY Ở ĐÂY

    -- 2. Rating ngẫu nhiên (từ 7 đến 10)
    floor(r4 * 4 + 7)::int,

    -- 3. movie_id ngẫu nhiên (từ 1 đến 5)
    floor(r5 * 5 + 1)::int,

    -- 4. user_id ngẫu nhiên (từ 11 đến 1008)
    floor(r6 * 998 + 11)::bigint,

    -- 5. Thời gian ngẫu nhiên trong vòng 10 ngày qua
    NOW() - (r7 * INTERVAL '10 days')
FROM (
    -- Subquery sinh giá trị ngẫu nhiên độc lập cho từng dòng trong 500 dòng
    SELECT
    random() AS r1, random() AS r2, random() AS r3,
    random() AS r4, random() AS r5, random() AS r6, random() AS r7
    FROM generate_series(1, 500)
    ) temp;