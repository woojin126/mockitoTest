package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

/*
@Mock 은 실제 객체인것처럼 행동
 */
class Test07verifyingBehavior {

    private BookingService bookingService;
    private PaymentService paymentServiceMock;
    private RoomService roomServiceMock;
    private BookingDAO bookingDAOMock;
    private MailSender mailSenderMock;

    @BeforeEach
    void setUp() {
        this.paymentServiceMock = mock(PaymentService.class);
        this.roomServiceMock = mock(RoomService.class);
        this.bookingDAOMock = mock(BookingDAO.class);
        this.mailSenderMock = mock(MailSender.class);
        this.bookingService = new BookingService(paymentServiceMock,roomServiceMock,bookingDAOMock,mailSenderMock);
        /*일반적으로 서비스단 테스트를 위해서 4개의 의존성을 가진다. 현재 MailSender 내부를보면 구현조차 되어있지않아 MailSender의 메서드를 사용하게되면 excepion이 발생한다.
          우리는 테스트 예약 서비스 자체에만 집중하고싶다. 그래서우린 모의 객체를 만든다.
         */
    }

    /*
    모의객체가 특정 입력에 대해 어떻게 동작하는지 아는것도 중요하다. (특정ㅁ ㅔ서드가 잘 호출이되는지)
    예를들어 결제서비스에서 특정메서드가 올바르게 호출이되었는지?, 예약신청이 서비스가 재대로 실행되었는지?,
     */
    @Test
    void should_InvokePayment_When_Prepaid() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 05), 2, true);

        //when
        bookingService.makeBooking(bookingRequest);

        //then
        verify(paymentServiceMock).pay(bookingRequest, 400.0);
        verify(paymentServiceMock,times(1)).pay(bookingRequest, 400.0);// times 한번만 호출되었는지확인
    }

    @Test
    void should_InvokePayment_When_NotPrepaid() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 05), 2, false);

        //when
        bookingService.makeBooking(bookingRequest);

        // paymentService가 단한번도 호출이 된적이없고 pay() 메서드가 호출된 적이 없음을 확인할 수 있다. (prepaid == false)
       //then
        verify(paymentServiceMock,never()).pay(any(), anyDouble());
    }
}
