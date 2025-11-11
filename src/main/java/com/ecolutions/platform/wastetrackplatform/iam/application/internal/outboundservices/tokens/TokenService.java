package com.ecolutions.platform.wastetrackplatform.iam.application.internal.outboundservices.tokens;


/**
 * TokenService
 * <p>
 *     Interface for token service.
 *     This service is used to generate, validate and extract information from tokens.
 * </p>
 */
public interface TokenService {
    /**
     * Generate a token for a given email.
     * @param username the email to generate the token for
     * @return the generated token
     */
    String generateToken(String username);

    /**
     * Generate an activation token for user account activation.
     * @param email the email to generate the activation token for
     * @return the generated activation token
     */
    String generateActivationToken(String email);

    /**
     * Generate a password reset token for password reset.
     * @param email the email to generate the password reset token for
     * @return the generated password reset token
     */
    String generatePasswordResetToken(String email);

    /**
     * Extract the email from a token.
     * @param token the token to extract the email from
     * @return the email extracted from the token
     */
    String getEmailFromToken(String token);

    /**
     * Validate a token.
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Get the purpose of the token (e.g., "auth", "activation", "password_reset").
     * @param token the token to get the purpose from
     * @return the purpose of the token, or null if not present
     */
    String getTokenPurpose(String token);
}
