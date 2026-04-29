FROM python:3.13-slim AS builder

# Dependencies
ENV UV_LINK_MODE=copy \
    UV_COMPILE_BYTECODE=1 \
    UV_PYTHON_DOWNLOADS=never \
    PYTHONFAULTHANDLER=1

COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

WORKDIR /app
RUN apt update && apt install -y build-essential
# COPY pyproject.toml uv.lock ./
COPY pyproject.toml ./
# RUN --mount=type=cache,target=/root/.cache/uv \
#     --mount=type=bind,source=uv.lock,target=uv.lock \
#     --mount=type=bind,source=pyproject.toml,target=pyproject.toml \
#     uv sync --no-dev --locked --no-editable --no-install-project

RUN uv sync

# Build static site
COPY . .
RUN uv run mkdocs build
# ---- Runtime stage ----
FROM nginx:alpine

# Remove default nginx static files
RUN rm -rf /usr/share/nginx/html/*

# Copy built site from builder
COPY --from=builder /app/build/docs /usr/share/nginx/html

# Expose HTTP port
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]