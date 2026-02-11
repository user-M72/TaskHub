package App.TaskHub.dto.req;

public record ProfileUpdateRequest(

        String username,
        String email,
        String currentPassword,
        String newPassword
) {
}
