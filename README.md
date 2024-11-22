# FitWell Backend

## 프로젝트 소개 📋
FitWell의 백엔드 서버는 Spring Boot와 MySQL을 기반으로 개인 맞춤형 건강 관리 서비스를 제공합니다.  
사용자 정보를 저장하고, 식단 및 운동 데이터를 관리하며, 피드백을 생성하는 RESTful API를 제공합니다.  
**카카오 로그인 API**를 활용하여 간편한 인증 기능을 제공합니다.

---

## 기술 스택 🔧
- **언어**: Java
- **프레임워크**: Spring Boot
- **데이터베이스**: MySQL
- **API**: 카카오 로그인 API, ChatGPT API
- **빌드 도구**: Maven
- **테스트**: JUnit 5, AssertJ
- **배포**: Docker, AWS EC2

---

## 프로그램 로직

![다이어그램](https://github.com/user-attachments/assets/823a3a1e-0b86-4d08-b2fe-11fed530a36c)

---

## 데이터베이스 설계 🗄️

### 1. User Table
| **Column**       | **Type**        | **Description**                |
|-------------------|-----------------|--------------------------------|
| `id`             | BIGINT          | Primary Key                   |
| `email`          | VARCHAR(255)    | 사용자 이메일                  |
| `nickname`       | VARCHAR(255)    | 사용자 닉네임                  |
| `height`         | FLOAT           | 키 (cm)                        |
| `weight`         | FLOAT           | 몸무게 (kg)                    |
| `age`            | INT             | 나이                           |
| `gender`         | ENUM('M', 'F')  | 성별                           |
| `health_goal`    | VARCHAR(255)    | 건강 목표                      |

### 2. Meal_Log Table
| **Column**       | **Type**        | **Description**                |
|-------------------|-----------------|--------------------------------|
| `id`             | BIGINT          | Primary Key                   |
| `user_id`        | BIGINT          | User Table의 Foreign Key       |
| `meal_name`      | VARCHAR(255)    | 식사 이름                      |
| `calories`       | FLOAT           | 칼로리                          |
| `date`           | DATE            | 기록 날짜                       |

### 3. Exercise_Log Table
| **Column**       | **Type**        | **Description**                |
|-------------------|-----------------|--------------------------------|
| `id`             | BIGINT          | Primary Key                   |
| `user_id`        | BIGINT          | User Table의 Foreign Key       |
| `exercise_type`  | VARCHAR(255)    | 운동 종류                      |
| `duration`       | FLOAT           | 운동 시간 (분)                 |
| `calories_burned`| FLOAT           | 소모된 칼로리                  |
| `date`           | DATE            | 기록 날짜                       |

### 4. Feedback Table
| **Column**       | **Type**        | **Description**                |
|-------------------|-----------------|--------------------------------|
| `id`             | BIGINT          | Primary Key                   |
| `user_id`        | BIGINT          | User Table의 Foreign Key       |
| `message`        | TEXT            | ChatGPT API를 통해 생성된 피드백|

---
