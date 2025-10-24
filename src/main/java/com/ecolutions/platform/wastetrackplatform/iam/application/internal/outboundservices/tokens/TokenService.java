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
     * Extract the email from a token.
     * @param token the token to extract the email from
     * @return the email extracted from the token
     */
    String getUsernameFromToken(String token);
    /**
     * Validate a token.
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}
