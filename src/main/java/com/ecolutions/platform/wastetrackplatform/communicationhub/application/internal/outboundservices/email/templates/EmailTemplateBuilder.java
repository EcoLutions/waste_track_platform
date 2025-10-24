package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.email.templates;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {
    public String buildUserInvitationTemplate(String email, String temporaryPassword, String roleName) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                    .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                    .credentials { background-color: #fff; padding: 15px; border-left: 4px solid #4CAF50; margin: 20px 0; }
                    .password { font-size: 18px; font-weight: bold; color: #4CAF50; letter-spacing: 1px; font-family: monospace; }
                    .warning { background-color: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>🌍 Welcome to WasteTrack Platform!</h1>
                </div>
                <div class="content">
                    <p>Hello,</p>
                    <p>Your account has been successfully created. You have been assigned the role of <strong>%s</strong>.</p>
                   \s
                    <div class="credentials">
                        <h3>📧 Your Login Credentials:</h3>
                        <p><strong>Email:</strong> %s</p>
                        <p><strong>Temporary Password:</strong></p>
                        <p class="password">%s</p>
                    </div>
                   \s
                    <div class="warning">
                        <p><strong>⚠️ Security Notice:</strong> You will be required to change this password upon your first login.</p>
                    </div>
                   \s
                    <p>If you didn't request this account, please contact our support team immediately.</p>
                   \s
                    <p>Best regards,<br><strong>The WasteTrack Team</strong></p>
                </div>
                <div class="footer">
                    <p>This is an automated message. Please do not reply to this email.</p>
                    <p>&copy; 2025 WasteTrack Platform. All rights reserved.</p>
                </div>
            </body>
            </html>
           \s""".formatted(roleName, email, temporaryPassword);
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
