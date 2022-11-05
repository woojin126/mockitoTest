package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;

/*
@Mock 은 실제 객체인것처럼 행동
 */
class Test02DefaultReturnValues {

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

        // mock의 기본 값확인 1.empty list 2.null Object 3. 0/false primitives
        System.out.println("List returned " + roomServiceMock.getAvailableRooms());
        System.out.println("Object returned " + roomServiceMock.findAvailableRoomId(null));
        System.out.println("Primitive returned " + roomServiceMock.getRoomCount());
    }


    @Test
    void should_CountAvailablePlaces() {
        //given
        int expected = 0;

        //when
        int actual = bookingService.getAvailablePlaceCount();

        //then
        Assertions.assertEquals(expected, actual);
    }
}
