package com.personalFinance.personal_finance.user.infrastructure.external.email.strategy;

import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.PasswordChangedEmailDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PasswordChangedEmailStrategy implements UserNotificationStrategy<PasswordChangedEmailDTO> {

    // Define o formato da data e hora
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public String getSubject() {
        return "Sua senha foi alterada - Personal Finance";
    }

    @Override
    public String buildContent(PasswordChangedEmailDTO dto) {
        // Formata a data e hora atual
        String formattedDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Confirmação de Alteração de Senha</title>
                <style>
                    /* Resets e Mobile Adaptation */
                    body, table, td, a { -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%; }
                    table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }
                    table { border-collapse: collapse !important; }
                    body { height: 100%% !important; margin: 0 !important; padding: 0 !important; width: 100%% !important; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
                    
                    /* Media Query para Responsividade */
                    @media screen and (max-width: 600px) {
                        .email-container { width: 100%% !important; }
                        .padding-box { padding: 20px !important; }
                    }
                </style>
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f4f4;">
                
                <table border="0" cellpadding="0" cellspacing="0" width="100%%">
                    <tr>
                        <td align="center" style="padding: 20px 0;">
                            
                            <table class="email-container" border="0" cellpadding="0" cellspacing="0" width="600" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                
                                <tr>
                                    <td align="center" style="background-color: #2196F3; padding: 30px; color: #ffffff;">
                                        <h1 style="margin: 0; font-size: 28px; font-weight: bold;">🔐 Senha Alterada!</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="padding-box" style="padding: 40px; color: #333333; font-size: 16px; line-height: 1.6;">
                                        <p style="margin-top: 0;">Olá, <strong>%s</strong>!</p>

                                        <div style="background-color: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; margin: 25px 0; border-radius: 4px;">
                                            <p style="margin: 0;"><strong>✅ Confirmação: Sua senha foi alterada com sucesso.</strong></p>
                                            <p style="margin: 10px 0 0 0; font-size: 14px;">Data e hora da alteração: <strong style="color: #0d47a1;">%s</strong></p>
                                        </div>

                                        <p>Esta é uma notificação importante sobre a segurança da sua conta no <strong>Personal Finance</strong>.</p>

                                        <div style="background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 4px; font-size: 15px;">
                                            <p style="margin: 0;"><strong>⚠️ Você não solicitou esta alteração?</strong></p>
                                            <p style="margin: 10px 0 0 0;">Se você não realizou esta alteração de senha, **aja imediatamente**:</p>
                                            <ul style="margin: 10px 0 0 0; padding-left: 20px;">
                                                <li style="margin-bottom: 5px;">Acesse a tela de **recuperação de senha** para redefini-la novamente.</li>
                                                <li>Entre em contato com nosso **suporte** imediatamente.</li>
                                            </ul>
                                        </div>

                                        <p>Se foi você quem alterou, pode ficar tranquilo(a). Sua conta está segura!</p>

                                        <p style="margin-top: 30px; font-size: 14px; color: #666;">Obrigado por priorizar sua segurança.</p>
                                        <p>Atenciosamente,<br><strong>Equipe Personal Finance</strong></p>
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
            """.formatted(dto.firstName(), formattedDate);
    }
}