package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
@Mock 은 실제 객체인것처럼 행동
 */
class Test09MockingVoidMethods {

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
    1.void 메소드에서 예외를 발생시켜려면 doThrow.. when 패턴을 사용
    2.doNothing을 사용하여 void 메서드가 아무 것도 하지 않는지 확인할 수 있다.
     */

    @Test
    void should_ThrowException_When_MailNotReady() {
     //given
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 05), 2, false);

        when(roomServiceMock.findAvailableRoomId(bookingRequest)).thenThrow(BusinessException.class);

        //사용할수없는이유는 void값을 반환하는 메서드와 when은 함께 사용할수 없기떄문
       //when(this.mailSenderMock.sendBookingConfirmation(any())).thenThrow(BusinessException.class);
        //위대신에
        doThrow(new BusinessException()).when(mailSenderMock).sendBookingConfirmation(any());
        //when
        Executable executable = () -> bookingService.makeBooking(bookingRequest);

        Assertions.assertThrows(BusinessException.class, executable);
    }

    //void 메서드가 예외를 throw 하지 않도록 하려는 경우에도 작성 패턴이있음
    @Test
    void should_NotThrowException_When_MailNotReady() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 01, 01), LocalDate.of(2020, 01, 05), 2, false);

        doNothing().when(mailSenderMock).sendBookingConfirmation(any());
        
        //when
        bookingService.makeBooking(bookingRequest);
    }
}
