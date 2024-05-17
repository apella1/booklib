package me.apella.booklib.feedback;

import me.apella.booklib.book.Book;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest feedbackRequest) {
        return Feedback
                .builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(Book.builder().id(feedbackRequest.bookId()).build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse
                .builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(feedback.getCreatedBy().equals(id))
                .build();
    }
}
