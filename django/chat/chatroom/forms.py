from django import forms

class RegisterForm(forms.Form):
    username = forms.CharField(
        required=True,
        max_length=35,
        widget=forms.TextInput(
            attrs={
                'class': 'text-field input-top',
                'placeholder': 'Username'
            }
        )
    )

    password = forms.CharField(
        required=True,
        max_length=200,
        widget=forms.PasswordInput(
            attrs={
                'class': 'text-field input-bottom',
                'placeholder': 'Password'
            }
        )
    )

class LoginForm(forms.Form):
    username = forms.CharField(
        required=True,
        max_length=35,
        widget=forms.TextInput(
            attrs={
                'class': 'text-field input-top',
                'placeholder': 'Username'
            }
        )
    )

    password = forms.CharField(
        required=True,
        max_length=200,
        widget=forms.PasswordInput(
            attrs={
                'class': 'text-field input-bottom',
                'placeholder': 'Password'
            }
        )
    )
