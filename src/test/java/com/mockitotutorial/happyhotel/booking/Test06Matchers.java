package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
@Mock 은 실제 객체인것처럼 행동
 */
class Test06Matchers {

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
    1. 기본타입, anyDouble(), anyBoolean() 과같은 객체을이요하려면 any() 를사용해라
    2. 혼합된 일치 그리고, 정확한 값을 기입하려면 eq() 를사용해라 methd(any(), eq(400.0))
    3. null이 될수 있는 string,은 any() 를사용해라
    
     */
    @Test
    void should_NotCompleteBooking_When_PriceTooHigh    () {
     //given
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 05), 2, true);
        
        //모든 종류의 예약요청에 익셉션을 터뜨리고 싶을때
        //when(this.paymentServiceMock.pay(any(),anyDouble())).thenThrow(BusinessException.class);
        when(this.paymentServiceMock.pay(any(),eq(400.0))).thenThrow(BusinessException.class);

        //when
        Executable executable = () -> bookingService.makeBooking(bookingRequest);

        Assertions.assertThrows(BusinessException.class, executable);
    }



}
