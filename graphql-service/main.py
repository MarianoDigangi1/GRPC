import collections
import collections.abc
collections.MutableMapping = collections.abc.MutableMapping

from flask import Flask, request
from graphql_server.flask import GraphQLView
from app.resolvers import Query, Mutation
import graphene

schema = graphene.Schema(query=Query, mutation=Mutation)

app = Flask(__name__)
def context_function():
    user_id = request.headers.get("X-User-Id")
    return {"user_id": int(user_id) if user_id else None}
app.add_url_rule(
    "/graphql",
    view_func=GraphQLView.as_view(
        "graphql",
        schema=schema,
        graphiql=True  
    ),
)

if __name__ == "__main__":
    app.run(debug=True)
