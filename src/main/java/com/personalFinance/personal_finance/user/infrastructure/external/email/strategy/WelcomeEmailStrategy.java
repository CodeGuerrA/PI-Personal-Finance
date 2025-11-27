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
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #4CAF50; color: white; padding: 30px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                        .info-box { background-color: #e8f5e9; border-left: 4px solid #4CAF50; padding: 15px; margin: 20px 0; border-radius: 3px; }
                        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
                        .highlight { background-color: #fff; padding: 10px; border-radius: 3px; font-weight: bold; color: #4CAF50; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🎉 Bem-vindo ao Personal Finance!</h1>
                        </div>
                        <div class="content">
                            <p>Olá, <strong>%s</strong>!</p>
                
                            <p>É um prazer tê-lo conosco! Sua conta foi criada com sucesso e você já pode começar a organizar suas finanças pessoais.</p>
                
                            <div class="info-box">
                                <p style="margin: 0;"><strong>📝 Informações da sua conta:</strong></p>
                                <p style="margin: 10px 0 0 0;">Seu nome de usuário: <span class="highlight">%s</span></p>
                            </div>
                
                            <p><strong>🔐 Próximos passos:</strong></p>
                            <ol>
                                <li>Você recebeu uma senha temporária</li>
                                <li>Faça login com seu usuário e senha temporária</li>
                                <li>Defina sua senha permanente</li>
                                <li>Comece a usar o sistema!</li>
                            </ol>
                
                            <p><strong>💡 Dica:</strong> Para sua segurança, escolha uma senha forte contendo letras maiúsculas, minúsculas, números e caracteres especiais.</p>
                
                            <p>Se você tiver alguma dúvida, não hesite em entrar em contato conosco.</p>
                
                            <p>Boas-vindas e bom uso do sistema!</p>
                            <p><strong>Equipe Personal Finance</strong></p>
                        </div>
                        <div class="footer">
                            <p>Este é um email automático. Por favor, não responda.</p>
                            <p>&copy; 2025 Personal Finance. Todos os direitos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(welcomeEmailDTO.username(), welcomeEmailDTO.username(), welcomeEmailDTO.temporaryPassword());
    }
}
