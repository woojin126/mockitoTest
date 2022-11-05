package com.mockitotutorial.happyhotel.booking;

import java.time.temporal.ChronoUnit;

public class BookingService {

	private final PaymentService paymentService; //돈관리 서비스
	private final RoomService roomService; // 방관리 서비스
	private final BookingDAO bookingDAO; // 모든 예약 목록을 유지하는 repo
	private final MailSender mailSender; // 메일처리

	private final static double BASE_PRICE_USD = 50.0;

	//1박을 할 수 있는 투숙객 수를 확인할수있는 메서드
	public int getAvailablePlaceCount() {
		return roomService.getAvailableRooms()
				.stream()
				.map(room -> room.getCapacity())
				.reduce(0, Integer::sum);
	}
	// 예약 요청에 따라 객실 요금을 계산하는 메서드
	public double calculatePrice(BookingRequest bookingRequest) {
		long nights = ChronoUnit.DAYS.between(bookingRequest.getDateFrom(), bookingRequest.getDateTo());
		return BASE_PRICE_USD * bookingRequest.getGuestCount() * nights;
	}
	
	public double calculatePriceEuro(BookingRequest bookingRequest) {
		long nights = ChronoUnit.DAYS.between(bookingRequest.getDateFrom(), bookingRequest.getDateTo());
		return CurrencyConverter.toEuro(BASE_PRICE_USD * bookingRequest.getGuestCount() * nights);
	}
	//예약 요청이 전송되면 예약을 할 수 있고,
	public String makeBooking(BookingRequest bookingRequest) {
		String roomId = roomService.findAvailableRoomId(bookingRequest);
		double price = calculatePrice(bookingRequest);

		if (bookingRequest.isPrepaid()) {
			paymentService.pay(bookingRequest, price);
		}

		bookingRequest.setRoomId(roomId);
		String bookingId = bookingDAO.save(bookingRequest);
		roomService.bookRoom(roomId);
		mailSender.sendBookingConfirmation(bookingId);
		return bookingId;
	}
	//손님이 예약을 변경하면 예약을 취소 할 수 있는 메서드
	public void cancelBooking(String id) {
		BookingRequest request = bookingDAO.get(id);
		roomService.unbookRoom(request.getRoomId());
		bookingDAO.delete(id);
	}

	public BookingService(PaymentService paymentService, RoomService roomService, BookingDAO bookingDAO,
			MailSender mailSender) {
		super();
		this.paymentService = paymentService;
		this.roomService = roomService;
		this.bookingDAO = bookingDAO;
		this.mailSender = mailSender;
	}

}
