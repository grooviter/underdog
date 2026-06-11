# Stable Diffusion Client

Underdog's Stable Diffusion Client (sd-client) was born an HTTP client to access [stable-diffusion](https://github.com/leejet/stable-diffusion.cpp) server. There are different type 
of HTTP client APIs available:

- **sdapi**: This client exists for client compatibility with WebUI-style tools
- **sdcpp**: This is the native stable-diffusion.cpp API client
- **openai**: This family exists for client compatibility. Use it when you want OpenAI-style request and response shapes

--8<-- "docs/sd-client/sdapi.md"

--8<-- "docs/sd-client/sdcpp.md"

--8<-- "docs/sd-client/openai.md"