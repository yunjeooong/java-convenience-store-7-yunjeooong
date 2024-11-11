# java-convenience-store-precourse
# 구현 기능 목록

## 기능 (시스템)
### 💡- 재고 관리
- [x] 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인
- [x] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리
- [x] 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
### 💡프로모션 할인
- [x] 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용.
- [x] 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태.
- [x] 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않음. 
- [x] 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
- [x] 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용.
- [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우
  - [x] 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내
- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우
  - [x] 일부 수량에 대해 정가로 결제하게 됨을 안내
### 💡멤버십 할인
- [x] 멤버십 회원은 프로모션 미적용 금액의 30%를 할인.
- [x] 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- [x] 멤버십 할인의 최대 한도는 8,000원.
### 💡영수증 출력
- [x] 영수증은 고객의 구매 내역과 할인을 요약하여 출력
영수증 항목은 아래와 같다.
- [x] 구매 상품 내역: 구매한 상품명, 수량, 가격
- [x] 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
금액 정보
- [x] 총구매액: 구매한 상품의 총 수량과 총 금액
- [x] 행사할인: 프로모션에 의해 할인된 금액
- [x] 멤버십할인: 멤버십에 의해 추가로 할인된 금액
- [x] 내실돈: 최종 결제 금액
- [x] 보기 좋게 정렬



## 입력
- [x] 구매할 상품과 수량을 입력 받는다. 
  - [x] 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다. [콜라-10],[사이다-3]
    - [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
      - [x] 메세지: 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    - [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우: 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력
      - [x] 메세지: 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    - [x] 멤버십 할인 적용 
      - [x] 메세지: 멤버십 할인을 받으시겠습니까? (Y/N)
    - [x] 추가 구매여부
      - [x] 메세지: 감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
    



## 출력
- [x] 환영 메시지 및 상품 목록 출력
  src/main/resources/products.md과 src/main/resources/promotions.md
  두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.



## 예외
- [x] 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
- [x] 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
- [x] 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
- [x] 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

