package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
@Mock 은 실제 객체인것처럼 행동
 */
class Test10ArgumentCaptors {

    private BookingService bookingService;
    private PaymentService paymentServiceMock;
    private RoomService roomServiceMock;
    private BookingDAO bookingDAOMock;
    private MailSender mailSenderMock;
    private ArgumentCaptor<Double> doubleCaptor;

    @BeforeEach
    void setUp() {
        this.paymentServiceMock = mock(PaymentService.class);
        this.roomServiceMock = mock(RoomService.class);
        this.bookingDAOMock = mock(BookingDAO.class);
        this.mailSenderMock = mock(MailSender.class);
        this.bookingService = new BookingService(paymentServiceMock,roomServiceMock,bookingDAOMock,mailSenderMock);
        this.doubleCaptor = ArgumentCaptor.forClass(Double.class);
    }



    //방 에약시 지불할 금액이 400원인데. 캡처기를 사용해서 값을 캡쳐 할것이다.
    @Test
    void should_PayCorrectPrice_When_InputOK() {
     //given
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 05), 2, true);

        //when
        bookingService.makeBooking(bookingRequest);

        verify(paymentServiceMock, times(1)).pay(eq(bookingRequest),doubleCaptor.capture());
        double capturedArgument = doubleCaptor.getValue();

        System.out.println(capturedArgument);
    }

    //위의 예제를 여러번 호출시
    @Test
    void should_PayCorrectPrices_When_MultipleCalls() {
        //given
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 05), 2, true);
        BookingRequest bookingRequest1 = new BookingRequest("1", LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 02), 2, true);
        List<Double> expectedValues = Arrays.asList(400.0,100.0);
        //when
        bookingService.makeBooking(bookingRequest);
        bookingService.makeBooking(bookingRequest1);

        verify(paymentServiceMock, times(2)).pay(any(),doubleCaptor.capture());
        List<Double> captureArguments = doubleCaptor.getAllValues();
        Assertions.assertEquals(expectedValues, captureArguments);
    }


}
