# Проект по автоматизации UI, API, Mobile

## О проекте
Проект представляет собой комплексное решение для автоматизированного тестирования:
- Веб-интерфейсов (UI)
- REST API
- Мобильных приложений (Android)

## Технологии и инструменты

### Основной стек:
| Технология | Назначение |
|------------|------------|
| Gradle | Сборщик проекта | 
| JUnit 5 | Фреймворк для тестирования | 
| Selenide | Автоматизация веб-интерфейсов |
|Docker+Selenoid|Контейнер|Контейнеры браузеров|
| REST Assured | Тестирование REST API | 
| Appium Java Client | Мобильная автоматизация | 

### Дополнительные библиотеки:
- AssertJ - улучшенные assertions
- WebDriverManager - управление драйверами браузеров
- Jackson Databind - работа с JSON
- Owner - работа с конфигурационными файлами
- JavaFaker - генерация тестовых данных
- SLF4J Simple - логирование
- Allure Report - система отчетности

## Запуск тестов

### Основная команда:
```bash
gradle clean test -Dtag=<tag> -DrunIn=<runIn>
```
### Параметры:
| Параметр | Допустимые значения | Описание |
|------------|------------|--------|
|tag|UI, API, MOBILE|обязательный|
|runIn|browser_local, browser_selenoid, mobile|обязательный|

### Примеры:
UI тесты в локальном браузере
```bash
gradle clean test -Dtag=UI -DrunIn=browser_local
```
API тесты
```bash
gradle clean test -Dtag=API
```
Mobile тесты на Android
```bash
gradle clean test -Dtag=MOBILE -DrunIn=mobile
```
### Требования к окружению:
#### Для Selenoid:
- Установить Docker
 - Запустите Selenoid
 ```bash
cd /selenoid/cm
```
```bash
./cm selenoid start --vnc
```
```bash
./cm selenoid-ui start
```
[При проблемах см. документацию Selenoid](https://aerokube.com/cm/latest/)
#### Для мобильного тестирования:
- Установить Android Studio
- Установить Appium Server GUI
- Установить Appium Inspector

### Конфигурация:
Конфигурационные файлы расположены в:
```bash
cd /src/test/resources/configs/
```
### Доступные конфиги:
- ui.properties - настройки UI тестов
- api.properties - настройки API тестов
- mobile.properties - настройки мобильных тестов
