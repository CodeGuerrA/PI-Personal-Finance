package com.personalFinance.personal_finance.user.infrastructure.external.email.strategy;

import com.personalFinance.personal_finance.user.infrastructure.external.email.dto.WelcomeEmailDTO;
import org.springframework.stereotype.Component;

@Component
public class WelcomeEmailStrategy implements UserNotificationStrategy<WelcomeEmailDTO> {

    @Override
    public String getSubject() {
        return "Bem-vindo ao Personal Finance! 🎉";
    }

    @Override
    public String buildContent(WelcomeEmailDTO welcomeEmailDTO) {
        // Correção: Os símbolos de % no CSS foram alterados para %% para o Java não confundir com variáveis.
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Bem-vindo ao Personal Finance</title>
                <style>
                    /* Reset básico e Media Queries para Mobile */
                    body, table, td, a { -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%; }
                    table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }
                    img { -ms-interpolation-mode: bicubic; border: 0; height: auto; line-height: 100%%; outline: none; text-decoration: none; }
                    table { border-collapse: collapse !important; }
                    body { height: 100%% !important; margin: 0 !important; padding: 0 !important; width: 100%% !important; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            
                    /* Estilos responsivos */
                    @media screen and (max-width: 600px) {
                        .email-container { width: 100%% !important; }
                        .padding-box { padding: 20px !important; }
                        h1 { font-size: 22px !important; }
                    }
                </style>
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f4f4;">
            
                <table border="0" cellpadding="0" cellspacing="0" width="100%%">
                    <tr>
                        <td align="center" style="padding: 20px 0;">
            
                            <table class="email-container" border="0" cellpadding="0" cellspacing="0" width="600" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
            
                                <tr>
                                    <td align="center" style="background-color: #4CAF50; padding: 30px; color: #ffffff;">
                                        <h1 style="margin: 0; font-size: 28px; font-weight: bold;">Bem-vindo! 🎉</h1>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="padding-box" style="padding: 40px; color: #333333; font-size: 16px; line-height: 1.6;">
                                        <p style="margin-top: 0;">Olá, <strong>%s</strong>!</p>
                                        
                                        <p>É um prazer tê-lo conosco! Sua conta foi criada com sucesso e você já pode começar a organizar suas finanças pessoais de forma inteligente.</p>

                                        <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #e8f5e9; border-left: 5px solid #4CAF50; border-radius: 4px; margin: 25px 0;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="margin: 0 0 10px 0; color: #2E7D32;"><strong>📝 Suas Credenciais Temporárias</strong></p>
                                                    <p style="margin: 5px 0;">Usuário: <strong style="color: #333; background-color: #fff; padding: 2px 6px; border-radius: 3px;">%s</strong></p>
                                                    <p style="margin: 5px 0;">Senha: <strong style="color: #333; background-color: #fff; padding: 2px 6px; border-radius: 3px;">%s</strong></p>
                                                </td>
                                            </tr>
                                        </table>

                                        <p><strong>🔐 Próximos passos para sua segurança:</strong></p>
                                        <ol style="padding-left: 20px; color: #555;">
                                            <li style="margin-bottom: 8px;">Acesse o sistema com as credenciais acima.</li>
                                            <li style="margin-bottom: 8px;">Vá até o seu perfil.</li>
                                            <li style="margin-bottom: 8px;">Altere sua senha temporária para uma definitiva.</li>
                                        </ol>

                                        <p style="font-size: 14px; color: #666; background-color: #fff3cd; padding: 10px; border-radius: 4px; border: 1px solid #ffeeba;">
                                            <strong>💡 Dica:</strong> Escolha uma senha forte contendo letras maiúsculas, minúsculas, números e caracteres especiais.
                                        </p>
                                        
                                        <p style="margin-top: 30px;">Se tiver dúvidas, estamos à disposição.</p>
                                        <p>Atenciosamente,<br><strong>Equipe Personal Finance</strong></p>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="background-color: #f9f9f9; padding: 20px; font-size: 12px; color: #888888; border-top: 1px solid #eeeeee;">
                                        <p style="margin: 0 0 5px 0;">Este é um email automático, por favor não responda.</p>
                                        <p style="margin: 0;">&copy; 2025 Personal Finance. Todos os direitos reservados.</p>
                                    </td>
                                </tr>
                            </table>
                            
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(welcomeEmailDTO.username(), welcomeEmailDTO.username(), welcomeEmailDTO.temporaryPassword());
    }
}