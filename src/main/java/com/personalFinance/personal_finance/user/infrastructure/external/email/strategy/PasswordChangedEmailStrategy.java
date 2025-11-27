package com.personalFinance.personal_finance.user.infrastructure.external.email.strategy;

import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.PasswordChangedEmailDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PasswordChangedEmailStrategy implements UserNotificationStrategy<PasswordChangedEmailDTO> {
    @Override
    public String getSubject() {
        return "Sua senha foi alterada - Personal Finance";
    }

    @Override
    public String buildContent(PasswordChangedEmailDTO dto) {
        String formattedDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #2196F3; color: white; padding: 30px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                        .success-box { background-color: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; margin: 20px 0; border-radius: 3px; }
                        .warning-box { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 3px; }
                        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔐 Senha Alterada com Sucesso</h1>
                        </div>
                        <div class="content">
                            <p>Olá, <strong>%s</strong>!</p>

                            <div class="success-box">
                                <p style="margin: 0;"><strong>✅ Sua senha foi alterada com sucesso!</strong></p>
                                <p style="margin: 10px 0 0 0;">Data e hora: <strong>%s</strong></p>
                            </div>

                            <p>Esta é uma confirmação de que a senha da sua conta no <strong>Personal Finance</strong> foi alterada recentemente.</p>

                            <div class="warning-box">
                                <p style="margin: 0;"><strong>⚠️ Você não reconhece esta alteração?</strong></p>
                                <p style="margin: 10px 0 0 0;">Se você não solicitou esta alteração de senha, recomendamos que você:</p>
                                <ul style="margin: 10px 0 0 0; padding-left: 20px;">
                                    <li>Acesse imediatamente a recuperação de senha</li>
                                    <li>Entre em contato com nosso suporte</li>
                                    <li>Verifique se há acessos não autorizados</li>
                                </ul>
                            </div>

                            <p>Se foi você quem alterou a senha, pode ignorar este aviso. Sua conta está segura.</p>

                            <p><strong>💡 Dicas de segurança:</strong></p>
                            <ul>
                                <li>Nunca compartilhe sua senha com ninguém</li>
                                <li>Use senhas únicas para cada serviço</li>
                                <li>Altere sua senha periodicamente</li>
                                <li>Mantenha seu email de recuperação atualizado</li>
                            </ul>

                            <p>Obrigado por usar o Personal Finance!</p>
                            <p><strong>Equipe Personal Finance</strong></p>
                        </div>
                        <div class="footer">
                            <p>Este é um email automático. Por favor, não responda.</p>
                            <p>&copy; 2025 Personal Finance. Todos os direitos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(dto.firstName(), formattedDate);
    }
}
