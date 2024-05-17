package me.apella.booklib.feedback;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.apella.booklib.book.Book;
import me.apella.booklib.book.BookRepository;
import me.apella.booklib.common.PageResponse;
import me.apella.booklib.exception.OperationNotPermittedException;
import me.apella.booklib.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "No book found with the provided id: " + feedbackRequest.bookId()
                        )
                );
        User user = ((User) connectedUser.getPrincipal());

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException(
                    "The given book is archived or not shareable therefore no feedback can be given."
            );
        }

        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You cannot give feedback to your book");
        }

        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(
            Integer bookId,
            int page,
            int size,
            Authentication connectedUser
    ) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllFeedbacksByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks
                .stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
