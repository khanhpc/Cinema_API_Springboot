# Bước 1: Chọn máy chạy Java 21 (Bản JRE cho nhẹ, đủ để chạy)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Bước 2: Copy cục hàng (.jar) vào trong máy ảo
# Bác lưu ý: File .jar phải nằm cùng thư mục với Dockerfile này khi bác upload lên HF
COPY *.jar app.jar

# Bước 3: Mở cổng 7860 cho Hugging Face
EXPOSE 7860

# Bước 4: Lệnh khởi động "Thần thánh"
# Phải ép nó chạy Port 7860 và Address 0.0.0.0 thì HF mới nhìn thấy App của bác
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=7860", "--server.address=0.0.0.0"]