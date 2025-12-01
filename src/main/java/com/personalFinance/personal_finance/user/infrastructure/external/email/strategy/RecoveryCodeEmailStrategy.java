package com.personalFinance.personal_finance.user.infrastructure.external.email.strategy;

import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.RecoveryCodeEmailDTO;
import org.springframework.stereotype.Component;

@Component
public class RecoveryCodeEmailStrategy implements UserNotificationStrategy<RecoveryCodeEmailDTO> {

    @Override
    public String getSubject() {
        return "Código de recuperação de senha - Personal Finance";
    }

    @Override
    public String buildContent(RecoveryCodeEmailDTO dto) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Recuperação de Senha</title>
                <style>
                    /* Resets e Client-Specific Styles */
                    body, table, td, a { -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%; }
                    table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }
                    img { -ms-interpolation-mode: bicubic; border: 0; outline: none; text-decoration: none; }
                    table { border-collapse: collapse !important; }
                    body { height: 100%% !important; margin: 0 !important; padding: 0 !important; width: 100%% !important; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
                    
                    /* Responsividade */
                    @media screen and (max-width: 600px) {
                        .email-container { width: 100%% !important; }
                        .padding-box { padding: 20px !important; }
                        .code-text { font-size: 28px !important; letter-spacing: 3px !important; }
                        h1 { font-size: 24px !important; }
                    }
                </style>
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f4f4;">
                
                <table border="0" cellpadding="0" cellspacing="0" width="100%%">
                    <tr>
                        <td align="center" style="padding: 20px 0;">
                            
                            <table class="email-container" border="0" cellpadding="0" cellspacing="0" width="600" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                
                                <tr>
                                    <td align="center" style="background-color: #FF9800; padding: 30px; color: #ffffff;">
                                        <h1 style="margin: 0; font-size: 28px; font-weight: bold;">🔑 Recuperação</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="padding-box" style="padding: 40px; color: #333333; font-size: 16px; line-height: 1.6;">
                                        <p style="margin-top: 0;">Olá, <strong>%s</strong>!</p>
                                        
                                        <p>Recebemos uma solicitação para redefinir a senha da sua conta no <strong>Personal Finance</strong>.</p>
                                        
                                        <p style="margin-bottom: 10px;">Use o código abaixo para continuar:</p>

                                        <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="margin: 25px 0;">
                                            <tr>
                                                <td align="center" style="background-color: #fff3e0; border: 2px dashed #FF9800; border-radius: 8px; padding: 20px;">
                                                    <span class="code-text" style="font-family: 'Courier New', monospace; font-size: 36px; font-weight: bold; color: #e65100; letter-spacing: 8px; display: block;">%s</span>
                                                </td>
                                            </tr>
                                        </table>

                                        <div style="background-color: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; border-radius: 4px; font-size: 14px; margin-bottom: 20px;">
                                            <p style="margin: 0 0 5px 0;"><strong>⏱️ Fique atento:</strong></p>
                                            <ul style="margin: 5px 0 0 0; padding-left: 20px; color: #0d47a1;">
                                                <li>Este código expira em <strong>15 minutos</strong>.</li>
                                                <li>Ele é válido para apenas uma utilização.</li>
                                            </ul>
                                        </div>

                                        <div style="background-color: #ffebee; border-left: 4px solid #d32f2f; padding: 15px; border-radius: 4px; font-size: 14px; color: #b71c1c;">
                                            <p style="margin: 0;"><strong>🚨 Não foi você?</strong></p>
                                            <p style="margin: 5px 0 0 0;">Se você não solicitou a troca de senha, ignore este email. Sua conta permanece segura e sua senha atual não foi alterada.</p>
                                        </div>

                                        <p style="margin-top: 30px; font-size: 14px; color: #666;">
                                            <strong>Dica de segurança:</strong> Nunca compartilhe este código. Nossa equipe nunca pedirá sua senha ou código por telefone.
                                        </p>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="background-color: #f9f9f9; padding: 20px; font-size: 12px; color: #888888; border-top: 1px solid #eeeeee;">
                                        <p style="margin: 0 0 5px 0;">Este é um email automático de segurança.</p>
                                        <p style="margin: 0;">&copy; 2025 Personal Finance. Todos os direitos reservados.</p>
                                    </td>
                                </tr>
                            </table>
                            
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(dto.firstName(), dto.code());
    }
}