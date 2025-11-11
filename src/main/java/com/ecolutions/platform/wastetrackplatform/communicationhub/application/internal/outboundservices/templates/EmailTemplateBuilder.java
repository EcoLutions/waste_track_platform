package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.templates;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {
    public String buildUserInvitationTemplate(
            String email,
            String username,
            String activationUrl,
            String roleName,
            Long expirationDays
    ) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {\s
                        font-family: Arial, sans-serif;\s
                        line-height: 1.6;\s
                        color: #333;\s
                        max-width: 600px;\s
                        margin: 0 auto;\s
                        background-color: #f4f4f4;
                        padding: 20px;
                    }
                    .container {
                        background-color: white;
                        border-radius: 8px;
                        overflow: hidden;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    .header {\s
                        background: linear-gradient(135deg, #4CAF50 0%%, #45a049 100%%);
                        color: white;\s
                        padding: 30px 20px;\s
                        text-align: center;\s
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 28px;
                    }
                    .content {\s
                        padding: 30px;\s
                    }
                    .welcome-text {
                        font-size: 16px;
                        margin-bottom: 20px;
                    }
                    .info-box {
                        background-color: #f9f9f9;
                        padding: 20px;
                        border-left: 4px solid #4CAF50;
                        margin: 20px 0;
                    }
                    .info-box strong {
                        color: #4CAF50;
                    }
                    .button-container {
                        text-align: center;
                        margin: 30px 0;
                    }
                    .button {\s
                        display: inline-block;\s
                        padding: 15px 40px;\s
                        background: linear-gradient(135deg, #4CAF50 0%%, #45a049 100%%);
                        color: white !important;\s
                        text-decoration: none;\s
                        border-radius: 5px;
                        font-weight: bold;
                        font-size: 16px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.2);
                        transition: transform 0.2s;
                    }
                    .button:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 4px 8px rgba(0,0,0,0.3);
                    }
                    .warning {\s
                        background-color: #fff3cd;\s
                        padding: 15px;\s
                        border-left: 4px solid #ffc107;\s
                        margin: 20px 0;
                        border-radius: 4px;
                    }
                    .warning strong {
                        color: #856404;
                    }
                    .footer {\s
                        text-align: center;\s
                        padding: 20px;
                        background-color: #f9f9f9;
                        font-size: 12px;\s
                        color: #666;
                        border-top: 1px solid #eee;
                    }
                    .link-fallback {
                        word-break: break-all;
                        font-size: 12px;
                        color: #666;
                        margin-top: 15px;
                        padding: 10px;
                        background-color: #f9f9f9;
                        border-radius: 4px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🌍 Welcome to WasteTrack Platform!</h1>
                    </div>
                    <div class="content">
                        <p class="welcome-text">Hello <strong>%s</strong>,</p>
                        <p>You have been invited to join WasteTrack Platform as a <strong>%s</strong>.</p>
                      \s
                        <div class="info-box">
                            <p><strong>📧 Your Email:</strong> %s</p>
                            <p style="margin-top: 10px;">To complete your registration and set your password, please click the button below:</p>
                        </div>
                      \s
                        <div class="button-container">
                            <a href="%s" class="button">🔐 Activate My Account</a>
                        </div>
                      \s
                        <div class="link-fallback">
                            <p><small>If the button doesn't work, copy and paste this link into your browser:</small></p>
                            <p><small>%s</small></p>
                        </div>
                      \s
                        <div class="warning">
                            <p><strong>⏰ Important:</strong> This invitation link will expire in <strong>%d days</strong>.</p>
                            <p style="margin-top: 10px;"><strong>🔒 Security:</strong> You will create your own secure password during the activation process.</p>
                        </div>
                      \s
                        <p>If you didn't expect this invitation, please contact our support team immediately.</p>
                      \s
                        <p style="margin-top: 30px;">Best regards,<br><strong>The WasteTrack Team</strong></p>
                    </div>
                    <div class="footer">
                        <p>This is an automated message. Please do not reply to this email.</p>
                        <p>&copy; 2025 WasteTrack Platform. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
           \s""".formatted(username, roleName, email, activationUrl, activationUrl, expirationDays);
    }

    public String buildPasswordResetTemplate(String resetToken) {
        String resetLink = "https://wastetrack.com/reset-password?token=" + resetToken;

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
                    .header { background-color: #2196F3; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .button { display: inline-block; padding: 12px 30px; background-color: #2196F3; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .token { background-color: #fff; padding: 15px; border: 1px dashed #ccc; margin: 20px 0; font-family: monospace; word-break: break-all; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>🔐 Password Reset Request</h1>
                </div>
                <div class="content">
                    <p>We received a request to reset your password.</p>
                    <p>Click the button below to proceed:</p>
                    <center>
                        <a href="%s" class="button">Reset Password</a>
                    </center>
                    <p>Or copy and paste this link into your browser:</p>
                    <div class="token">%s</div>
                    <p><strong>⏰ This link will expire in 24 hours.</strong></p>
                    <p>If you didn't request this, please ignore this email. Your password will remain unchanged.</p>
                </div>
            </body>
            </html>
            """.formatted(resetLink, resetLink);
    }

    public String buildAccountActivationTemplate(String activationToken) {
        String activationLink = "https://wastetrack.com/activate?token=" + activationToken;

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
                    .header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 30px; }
                    .button { display: inline-block; padding: 12px 30px; background-color: #FF9800; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>✅ Activate Your Account</h1>
                </div>
                <div class="content">
                    <p>Welcome to WasteTrack!</p>
                    <p>Please click the button below to activate your account and set your password:</p>
                    <center>
                        <a href="%s" class="button">Activate Account</a>
                    </center>
                    <p>Or copy this link: %s</p>
                    <p><strong>⏰ This activation link expires in 24 hours.</strong></p>
                </div>
            </body>
            </html>
            """.formatted(activationLink, activationLink);
    }

    public String buildWelcomeTemplate(String userName) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 30px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>🎉 Welcome to WasteTrack!</h1>
                </div>
                <div class="content">
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Thank you for joining WasteTrack Platform. We're excited to have you on board!</p>
                    <p>Get started by exploring the platform and managing your waste tracking efficiently.</p>
                    <p>If you have any questions, feel free to reach out to our support team.</p>
                    <p>Best regards,<br><strong>The WasteTrack Team</strong></p>
                </div>
            </body>
            </html>
            """.formatted(userName);
    }
}
