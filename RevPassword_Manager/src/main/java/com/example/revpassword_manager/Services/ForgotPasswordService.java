package com.example.revpassword_manager.Services;

import com.example.revpassword_manager.DTOs.ResetPasswordRequest;
import com.example.revpassword_manager.DTOs.SecurityQuestionDTO;
import com.example.revpassword_manager.DTOs.UserQuestionAnswer;
import com.example.revpassword_manager.DTOs.VerifySecurityAnswersRequest;
import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.SecurityQuestionMaster;
import com.example.revpassword_manager.Models.SecurityQuestions;
import com.example.revpassword_manager.Reposiotory.SecurityQuestionRepository;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepo;
    private final SecurityQuestionRepository questionRepo;
    private final PasswordEncoder encoder;
   private final  SecurityQuestionRepository userQuestionRepo;

    public List<UserQuestionAnswer> getUserQuestionsWithMask(String username) {

        MasterUser user = userRepo.findByUsername(username).orElseThrow();

        List<SecurityQuestions> list = userQuestionRepo.findByUser(user);

        return list.stream().map(q -> {

            UserQuestionAnswer dto = new UserQuestionAnswer();

            dto.setQuestionId(q.getQuestion().getId());
            dto.setQuestion(q.getQuestion().getQuestion());
            dto.setAnswer("********"); // masked

            return dto;

        }).toList();
    }


    public boolean verifyAnswers(VerifySecurityAnswersRequest request) {

        String username = request.getUsername();
        Map<Long, String> answers = request.getAnswers();

        MasterUser user =
                userRepo.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        List<SecurityQuestions> list =
                userQuestionRepo.findByUser(user);

        int correct = 0;

        for (SecurityQuestions uq : list) {

            Long questionId = uq.getQuestion().getId();

            String provided = answers.get(questionId);

            if (provided != null &&
                    encoder.matches(
                            provided.toLowerCase().trim(),
                            uq.getAnswerHash())) {

                correct++;
            }
        }

        return correct == list.size(); // all must match
    }


    // STEP 3 — Reset Password
    public String resetPassword(
            ResetPasswordRequest request) {

        MasterUser user =
                userRepo.findByUsername(
                                request.getUsername())
                        .orElseThrow();

        user.setPasswordEncrypted(
                encoder.encode(
                        request.getNewPassword()));

        userRepo.save(user);

        return "Password Reset Successful";
    }

}
