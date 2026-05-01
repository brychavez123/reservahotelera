FROM eclipse-temurin:21-jdk AS buildstage

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src
COPY Wallet_UGNFM3WC8EM1NFQI /app/wallet

ENV WALLET_PATH=/app/wallet
ENV TNS_ADMIN=/app/wallet

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=buildstage /app/target/reservahotelera-0.0.1-SNAPSHOT.jar /app/reservahotelera.jar

COPY Wallet_UGNFM3WC8EM1NFQI /app/wallet

ENV WALLET_PATH=/app/wallet
ENV TNS_ADMIN=/app/wallet

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/reservahotelera.jar"]