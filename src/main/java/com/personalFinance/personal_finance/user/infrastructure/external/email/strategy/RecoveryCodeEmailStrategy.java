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
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #FF9800; color: white; padding: 30px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                        .code-box { background-color: #fff3e0; border: 2px solid #FF9800; padding: 20px; margin: 20px 0; border-radius: 5px; text-align: center; }
                        .code { font-size: 32px; font-weight: bold; color: #FF9800; letter-spacing: 5px; font-family: 'Courier New', monospace; }
                        .warning-box { background-color: #ffebee; border-left: 4px solid #f44336; padding: 15px; margin: 20px 0; border-radius: 3px; }
                        .info-box { background-color: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; margin: 20px 0; border-radius: 3px; }
                        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔑 Recuperação de Senha</h1>
                        </div>
                        <div class="content">
                            <p>Olá, <strong>%s</strong>!</p>

                            <p>Você solicitou a recuperação de senha da sua conta no <strong>Personal Finance</strong>.</p>

                            <div class="code-box">
                                <p style="margin: 0; font-size: 14px; color: #666;">Seu código de verificação é:</p>
                                <p class="code">%s</p>
                            </div>

                            <div class="info-box">
                                <p style="margin: 0;"><strong>⏱️ Informações importantes:</strong></p>
                                <ul style="margin: 10px 0 0 0; padding-left: 20px;">
                                    <li>Este código é válido por <strong>15 minutos</strong></li>
                                    <li>Use-o para redefinir sua senha</li>
                                    <li>Após o uso, o código será invalidado</li>
                                </ul>
                            </div>

                            <div class="warning-box">
                                <p style="margin: 0;"><strong>🚨 Você não solicitou esta recuperação?</strong></p>
                                <p style="margin: 10px 0 0 0;">Se você não solicitou a recuperação de senha, ignore este email. Sua senha permanecerá inalterada e sua conta estará segura.</p>
                            </div>

                            <p><strong>💡 Dicas de segurança:</strong></p>
                            <ul>
                                <li>Nunca compartilhe este código com ninguém</li>
                                <li>Nossa equipe nunca pedirá este código por telefone ou email</li>
                                <li>Se receber emails suspeitos, não clique em links</li>
                            </ul>

                            <p>Se precisar de ajuda, entre em contato com nosso suporte.</p>
                            <p><strong>Equipe Personal Finance</strong></p>
                        </div>
                        <div class="footer">
                            <p>Este é um email automático. Por favor, não responda.</p>
                            <p>&copy; 2025 Personal Finance. Todos os direitos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(dto.firstName(), dto.code());
    }
}
