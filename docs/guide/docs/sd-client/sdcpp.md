## sdcpp

The sdcpp client access the low level Stable Diffusion properties.

### Image Generation

TODO

### Image Editing

TODO

### Video Generation

This client supports using text-to-video (t2v) and image-to-video (i2v) modes. 

#### Text To Video - T2V

This mode generates video from a given prompt text and the settings passed to client.

#### Image To Video - I2V

This mode generates video from an example image which helps the prompt to explain the model
what to expect.

### Meta Information

Follow up there are a few endpoints giving helpful information required
for generation tasks.

#### Job Info

All actions in this client are tasks running in background. The only way to know the status
of a given tasks is to use the id return by the result of the task execution and use the following
method:

#### Job Cancel

If you'd like to cancel a given job, you can use this method with the id of the job.

#### Capabilities

The capabilities method returns all possible settings of the server.